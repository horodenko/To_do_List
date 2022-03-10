package com.example.to_do_list.ui

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do_list.databinding.ActivityAddTaskBinding
import com.example.to_do_list.datasource.TaskDataSource
import com.example.to_do_list.extensions.format
import com.example.to_do_list.extensions.text
import com.example.to_do_list.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // view binding: create classes for xml files
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // intent from Activity class
        if (intent.hasExtra(TASK_ID)) {
            // if it doesn't have TASK_ID, get zero
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.titleInput.text = it.title
                binding.descInput.text = it.desc
                binding.dateMenu.text = it.date
                binding.timeMenu.text = it.hour
            }
        }

        insertListeners()
    }

    private fun insertListeners(){
        binding.dateMenu.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()

            // when user press 'Ok' on date Picker menu
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset = timeZone.getOffset(Date().time) * -1
                binding.dateMenu.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.timeMenu.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                // .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
                val minute = if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.timeMenu.text = "${hour}:${minute}"
            }

            timePicker.show(supportFragmentManager, null)
        }
        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.createTaskBtn.setOnClickListener {
            // add
            val task = Task(
                title = binding.titleInput.text,
                desc = binding.descInput.text,
                date = binding.dateMenu.text,
                hour = binding.timeMenu.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }

}