package com.example.androidcreateprojecttest.test_chat_firebase.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidcreateprojecttest.data.model.Users
import com.example.androidcreateprojecttest.databinding.UserListChatBinding

class TestChatAdapter(
    private val list: ArrayList<Users>,
    private val callBackClick: CallBackItemClickListener
):
    RecyclerView.Adapter<TestChatAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBindView = UserListChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(mBindView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBinding(users = list[position], onClickCallBack = callBackClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(private val bind: UserListChatBinding) : RecyclerView.ViewHolder(bind.root){
       fun onBinding(users: Users, onClickCallBack : CallBackItemClickListener) {
           bind.edittextName.text = users.name
           bind.edittextEmail.text = users.email
           Glide.with(bind.imgProfile).load(users.image).into(bind.imgProfile)

           bind.mainLayout.setOnClickListener {
               onClickCallBack.clickItemCallBack(users)
           }
       }
    }

    interface CallBackItemClickListener {
        fun clickItemCallBack(usersItem : Users)
    }

    fun clear(){
        val size = list.size
        if (size > 0) {
            for (i in 0 until size) {
                list.removeAt(0)
            }
            notifyItemRangeRemoved(0, size)
        }
    }
}