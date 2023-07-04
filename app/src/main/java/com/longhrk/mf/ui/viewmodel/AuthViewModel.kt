package com.longhrk.mf.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.longhrk.data.database.entity.LocalUser
import com.longhrk.data.repo.DataRepo
import com.longhrk.mf.ui.viewmodel.model.ResultFireBase
import com.longhrk.mf.ui.viewmodel.model.UserAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val store: FirebaseFirestore,
    private val repo: DataRepo
) : ViewModel() {

    private var _isLogin = MutableStateFlow(false)
    val isLogin = _isLogin

    private var _isShowProgressBar = MutableStateFlow(false)
    val isShowProgressBar = _isShowProgressBar

    private val _statusResponse = MutableStateFlow(ResultFireBase.NONE)
    val statusResponse = _statusResponse

    private var _titleCurrent = MutableStateFlow("Login")
    val titleCurrent = _titleCurrent

    private var _currentUserAuth = MutableStateFlow<FirebaseUser?>(null)
    val currentUserAuth = _currentUserAuth

    private var _currentUserInLocal = MutableStateFlow<LocalUser?>(null)
    val currentUserInLocal = _currentUserInLocal

    private var _isShowUpdatePhoto = MutableStateFlow(false)
    val isShowUpdatePhoto = _isShowUpdatePhoto

    fun setCurrentUser(user: FirebaseUser?) {
        _currentUserAuth.value = user
    }

    fun isSkipLogin(email: String) = viewModelScope.async {
        val isSkip = repo.getIsLogin()
        if (isSkip) {
            checkUserAvailable(email)
            if (_currentUserAuth.value != null) {
                _isLogin.value = true
                setStatusShowUpdatePhoto()
            }
        }
    }

    suspend fun getUserInLocalStorage() = viewModelScope.async {
        _currentUserInLocal.value = repo.getUser()
    }

    private suspend fun checkUserAvailable(email: String) {
        val task = auth.fetchSignInMethodsForEmail(email).await()
        val result = task.signInMethods ?: emptyList<String>()
        if (result.isNotEmpty()) {
            _currentUserAuth.value = auth.currentUser
        }
    }

    fun setTitleCurrent(title: String) {
        _titleCurrent.value = title
    }

    fun setLogin() {
        repo.setIsLogin(true)
        addUserInLocal(_currentUserAuth.value)
        setStatusShowUpdatePhoto()
        _isLogin.value = true
    }

    fun updateUserFirebase(profileUpdates: UserProfileChangeRequest) {
        _isShowProgressBar.value = true
        _currentUserAuth.value!!.updateProfile(profileUpdates)
            .addOnCompleteListener{
                _currentUserAuth.value = auth.currentUser
                _isShowProgressBar.value = false
                _isShowUpdatePhoto.value = false
            }
            .addOnFailureListener{
                _isShowProgressBar.value = false
            }
    }

    fun signIn(userAuth: UserAuth) {
        _isShowProgressBar.value = true
        auth.signInWithEmailAndPassword(
            userAuth.email,
            userAuth.password
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUserAuth.value = auth.currentUser
                _statusResponse.value = ResultFireBase.SUCCESS
                setLogin()
                _isShowProgressBar.value = false
            } else {
                _statusResponse.value = ResultFireBase.FAIL
                _isShowProgressBar.value = false
            }
        }.addOnFailureListener {
            _statusResponse.value = ResultFireBase.ERROR
            _isShowProgressBar.value = false
        }
    }

    fun signUp(userAuth: UserAuth) {
        _isShowProgressBar.value = true
        auth.createUserWithEmailAndPassword(
            userAuth.email,
            userAuth.password
        ).addOnCompleteListener {
            if (it.isSuccessful) {
                _currentUserAuth.value = auth.currentUser
                _statusResponse.value = ResultFireBase.SUCCESS
                setLogin()
                _isShowProgressBar.value = false
            } else {
                _statusResponse.value = ResultFireBase.FAIL
                _isShowProgressBar.value = false
            }
        }.addOnFailureListener {
            _statusResponse.value = ResultFireBase.ERROR
            _isShowProgressBar.value = false
        }
    }

    private fun setStatusShowUpdatePhoto() {
        _isShowUpdatePhoto.value = _currentUserAuth.value?.photoUrl == null
    }

    suspend fun signInWithGoogleOrFacebook(credential: AuthCredential): AuthResult {
        return auth.signInWithCredential(credential).await()
    }

    private fun addUserInLocal(user: FirebaseUser?) {
        if (user == null) return
        viewModelScope.launch {
            val isUserIsExist = async { repo.checkUserIsExist(user.email ?: "") }
            if (!isUserIsExist.await()) {
                repo.addUser(
                    LocalUser(
                        id = user.uid,
                        photo = user.photoUrl,
                        name = user.displayName ?: "You",
                        email = user.email ?: "",
                        phoneNumber = user.phoneNumber ?: "",
                    )
                )
            }
        }
    }
}
