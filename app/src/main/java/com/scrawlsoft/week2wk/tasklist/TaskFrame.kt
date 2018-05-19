package com.scrawlsoft.week2wk.tasklist

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Point
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.TextView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.scrawlsoft.week2wk.R
import com.scrawlsoft.week2wk.model.TaskModel
import java.time.LocalDate

class TaskFrame(private val frame: View,
                private val uid: String,
                private val mainContainer: View,
                private val fab: FloatingActionButton) {
    private val taskText = frame.findViewById<TextView>(R.id.add_task_text)

    private var activeSnapshot: DocumentSnapshot? = null

    private lateinit var lastRevealPoint: Point

    init {
        taskText.setOnEditorActionListener { _, _, _ ->
            save()
            true
        }

        // Grab any touches on the background view, otherwise, the views behind it will get them.
        frame.setOnClickListener {
            @Suppress("UNUSED_EXPRESSION")
            true
        }
    }

    fun isShown() = frame.visibility == View.VISIBLE

    fun show(snapshot: DocumentSnapshot? = null, pt: Point? = null) {
        val cx = pt?.x ?: mainContainer.right-30
        val cy = pt?.y ?: mainContainer.bottom-60
        lastRevealPoint = Point(cx, cy)

        val finalRadius = Math.max(mainContainer.width, mainContainer.height)
        val anim = ViewAnimationUtils.createCircularReveal(frame, cx, cy, 0f, finalRadius.toFloat())
        frame.visibility = View.VISIBLE
        fab.setImageResource(R.drawable.ic_done_white_24dp)

        val task = snapshot?.toObject(TaskModel::class.java)
                ?: TaskModel("", LocalDate.now().toString(), false)
        taskText.setText(task.text, TextView.BufferType.NORMAL)
        taskText.requestFocus()

        anim.start()
        activeSnapshot = snapshot
    }

    fun hide() {
        val initialRadius = frame.width.toFloat()
        val anim = ViewAnimationUtils.createCircularReveal(
                frame, lastRevealPoint.x, lastRevealPoint.y, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                frame.visibility = View.GONE
                frame.clearFocus()
                fab.setImageResource(R.drawable.ic_add_white_24dp)
            }
        })
        anim.start()
        activeSnapshot = null
    }

    fun save() {
        val desc: String = taskText.text.toString()
        taskText.text = ""

        if (desc.isNotBlank()) {
            val task = activeSnapshot?.toObject(TaskModel::class.java)
                    ?: TaskModel(desc, LocalDate.now())
            task.text = desc

            val snapshot = activeSnapshot
            if (snapshot == null) {
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
            } else {
                snapshot.reference.set(task)
                        .addOnSuccessListener {
                            Snackbar.make(mainContainer, "Task saved: $desc", Snackbar.LENGTH_SHORT).show()
                            hide()
                        }
                        .addOnFailureListener {
                            Snackbar.make(mainContainer, "Failed to save task", Snackbar.LENGTH_INDEFINITE).show()
                        }
            }
        }
    }
}