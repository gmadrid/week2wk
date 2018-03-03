package com.scrawlsoft.week2wk.tasklist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.model.TaskModel
import java.time.LocalDate

class TaskFrame(private val frame: View,
                private val uid: String,
                private val mainContainer: View,
                private val fab: FloatingActionButton) {
    private val taskText = frame.findViewById<TextView>(R.id.add_task_text)

    init {
        taskText.setOnEditorActionListener { _, _, _ ->
            save()
            true
        }
    }

    fun isShown() = frame.visibility == View.VISIBLE

    fun show() {
        val cx = mainContainer.right - 30
        val cy = mainContainer.bottom - 60
        val finalRadius = Math.max(mainContainer.width, mainContainer.height)
        val anim = ViewAnimationUtils.createCircularReveal(frame, cx, cy, 0f, finalRadius.toFloat())
        frame.visibility = View.VISIBLE
        taskText.setText("", TextView.BufferType.NORMAL)
        taskText.requestFocus()
        fab.setImageResource(R.drawable.ic_done_white_24dp)
        anim.start()
    }

    fun hide() {
        val cx = mainContainer.right - 30
        val cy = mainContainer.bottom - 60
        val initialRadius = frame.width.toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(frame, cx, cy, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                frame.visibility = View.GONE
                frame.clearFocus()
                fab.setImageResource(R.drawable.ic_add_white_24dp)
            }
        })
        anim.start()
    }

    fun save() {
        val desc: String = taskText.text.toString()
        taskText.text = ""

        if (desc.isNotBlank()) {
            val task = TaskModel(desc, LocalDate.now())

            FirebaseFirestore.getInstance()
                    .collection("users").document(uid)
                    .collection("tasks").add(task)
                    .addOnSuccessListener {
                        Snackbar.make(mainContainer, "Task added: $desc", Snackbar.LENGTH_SHORT).show()
                        hide()
                    }
                    .addOnFailureListener {
                        Snackbar.make(mainContainer, "Failed to add task", Snackbar.LENGTH_INDEFINITE).show()
                    }
        }
    }
}