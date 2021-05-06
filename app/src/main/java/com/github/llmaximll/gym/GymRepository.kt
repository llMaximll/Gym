package com.github.llmaximll.gym

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.github.llmaximll.gym.dataclasses.Images
import com.github.llmaximll.gym.dataclasses.Lessons
import com.github.llmaximll.gym.dataclasses.Profile
import com.github.llmaximll.gym.network.NetworkService
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val TAG = "Repository"

class GymRepository {

    fun signIn(username: String, password: String, cosmeticView: CosmeticView): Map<String, Map<String, Int>>? {
        var data: Map<String, Map<String, Int>>? = mapOf()

        NetworkService.instance
                ?.getJSONApi()
                ?.signIn(username, password)
                ?.enqueue(object : Callback<Map<String, Map<String, Int>>> {
                    override fun onResponse(call: Call<Map<String, Map<String, Int>>>?, response: Response<Map<String, Map<String, Int>>>?) {
                        data = response?.body()
                        val token: Int = data?.get("notice")?.get("token")!!
                        cosmeticView.message.value = "Успешно!"
                        cosmeticView.isSuccessful.value = true
                        cosmeticView.token.value = token
                    }

                    override fun onFailure(call: Call<Map<String, Map<String, Int>>>?, t: Throwable?) {
                        cosmeticView.isSuccessful.value = false
                        when (t?.message) {
                            "java.lang.NumberFormatException: For input string: \"Error username or password\"" -> {
                                cosmeticView.message.value = "Неверный логин или пароль"
                            }
                            "java.lang.NumberFormatException: For input string: \"User is active\"" -> {
                                cosmeticView.message.value = "Пользователь уже активен"
                            }
                            else -> {
                                cosmeticView.message.value = t?.message
                            }
                        }
                    }
                })

        return data
    }

    fun signUp(name: String, mail: String, password: String,
               height: String, weight: String, cosmeticView: CosmeticView): Map<String, Map<String, String>>? {
        var data: Map<String, Map<String, String>>? = mapOf()

        NetworkService.instance
                ?.getJSONApi()
                ?.signUp(name, mail, password, height, weight)
                ?.enqueue(object : Callback<Map<String, Map<String, String>>> {
                    override fun onResponse(call: Call<Map<String, Map<String, String>>>?,
                                            response: Response<Map<String, Map<String, String>>>?) {
                        data = response?.body()
                        cosmeticView.isSuccessful.value = true
                        cosmeticView.message.value = "Успешно!"
                    }

                    override fun onFailure(call: Call<Map<String, Map<String, String>>>?, t: Throwable?) {
                        cosmeticView.isSuccessful.value = false
                        cosmeticView.message.value = t?.message
                    }
                })

        return data
    }

    fun getLessons(cosmeticView: CosmeticView) {
        var data: List<Lessons>?
        NetworkService.instance
                ?.getJSONApi()
                ?.getLessons()
                ?.enqueue(object : Callback<List<Lessons>> {
                    override fun onResponse(call: Call<List<Lessons>>?, response: Response<List<Lessons>>?) {
                        data = response?.body()
                        cosmeticView.isSuccessful.value = true
                        cosmeticView.message.value = "Успешно!"
                        cosmeticView.lessons.value = data
                    }

                    override fun onFailure(call: Call<List<Lessons>>?, t: Throwable?) {
                        cosmeticView.isSuccessful.value = false
                        cosmeticView.message.value = t?.message
                    }
                })
    }

    fun getProfile(token: String, profile: MutableLiveData<List<Profile>>, cosmeticView: CosmeticView) {
        NetworkService.instance
                ?.getJSONApi()
                ?.getProfile(token)
                ?.enqueue(object : Callback<List<Profile>> {
                    override fun onResponse(call: Call<List<Profile>>?, response: Response<List<Profile>>?) {
                        profile.value = response?.body()
                        cosmeticView.isSuccessful.value = true
                    }

                    override fun onFailure(call: Call<List<Profile>>?, t: Throwable?) {
                        cosmeticView.isSuccessful.value = false
                        cosmeticView.message.value = t?.message
                    }
                })
    }

    fun signOut(username: String, cosmeticView: CosmeticView) {
        NetworkService.instance
                ?.getJSONApi()
                ?.signOut(username)
                ?.enqueue(object : Callback<Map<String, Map<String, String>>> {
                    override fun onResponse(call: Call<Map<String, Map<String, String>>>?, response: Response<Map<String, Map<String, String>>>?) {
                        when (val mes = response?.body()?.get("notice")?.get("text")) {
                            "User log out" -> cosmeticView.message.value = "Успешно!"
                            "User is not log in" -> cosmeticView.message.value = "Пользователь не в аккаунте"
                            else -> cosmeticView.message.value = mes
                        }
                        cosmeticView.isSuccessful.value = true
                    }

                    override fun onFailure(call: Call<Map<String, Map<String, String>>>?, t: Throwable?) {
                        cosmeticView.isSuccessful.value = false
                        cosmeticView.message.value = t?.message
                    }
                })
    }

    fun editProfile(token: String, weight: String, height: String, cosmeticView: CosmeticView) {
        NetworkService.instance
                ?.getJSONApi()
                ?.editProfile(token, weight, height)
                ?.enqueue(object : Callback<Map<String, Map<String, String>>> {
                    override fun onResponse(call: Call<Map<String, Map<String, String>>>?, response: Response<Map<String, Map<String, String>>>?) {
                        val data = response?.body()
                        cosmeticView.isSuccessful.value = true
                        when (data?.get("notice")?.get("text")) {
                            "User update" -> cosmeticView.message.value = "Данные обновлены"
                            else -> cosmeticView.message.value = data?.get("notice")?.get("text")
                        }
                    }

                    override fun onFailure(call: Call<Map<String, Map<String, String>>>?, t: Throwable?) {
                        cosmeticView.isSuccessful.value = false
                        cosmeticView.message.value = t?.message
                    }
                })
    }

    fun downloadImage(url: String, category: String, image: MutableLiveData<Images>) {
        var count = 0
        NetworkService.instance
                ?.getJSONApi()
                ?.downloadImage(url)
                ?.enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                        if (response?.isSuccessful == true) {
                            val data = response.body()
                            val stream = data?.byteStream()
                            val bitmap: Bitmap = BitmapFactory.decodeStream(stream)
                            image.value = Images(bitmap, category)
                            count++
                        } else {
                            try {
                                Log.d("TAG", "response error: " + response?.errorBody()?.string().toString())
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }
                    override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                        log(TAG, "failure | ${t?.message}")
                    }
                })
    }

    private fun log(tag: String, message: String) {
        if (BuildConfig.DEBUG) {
            Log.i(tag, message)
        }
    }
}