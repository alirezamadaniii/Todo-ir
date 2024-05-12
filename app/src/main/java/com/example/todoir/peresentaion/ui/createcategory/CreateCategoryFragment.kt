package com.example.todoir.peresentaion.ui.createcategory

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.todoir.R
import com.example.todoir.data.model.Category
import com.example.todoir.data.model.CategoryColor
import com.example.todoir.data.model.Icon
import com.example.todoir.data.utils.dialog
import com.example.todoir.databinding.FragmentCreateCategoryBinding
import com.example.todoir.peresentaion.adapter.ColorAdapter
import com.example.todoir.peresentaion.adapter.IconAdapter
import com.example.todoir.peresentaion.viewmodel.CreateCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCategoryFragment : Fragment() {

    private lateinit var binding: FragmentCreateCategoryBinding
    private var icon:Int = 0
    private var color:String = ""

    private val viewModel:CreateCategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(layoutInflater,R.layout.fragment_create_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showColor()
        onCLick()

    }



    private fun onCLick() {
        binding.imgChooseIcon.setOnClickListener {
            showIconDialog()
        }

        binding.btnCreateCategory.setOnClickListener {
            val categoryName = binding.edtCategoryName.text.toString()
            val categoryIcon = icon
            val categoryColor = color
            viewModel.addCategory(Category(0,categoryName,categoryIcon,categoryColor))
            findNavController().popBackStack()
        }
    }


    private fun showColor() {
        val adapter = ColorAdapter()
        adapter.differ.submitList(createColorList())
        binding.recyCategoryColor.adapter = adapter

        adapter.setOnItemClick {
            color = it.color
        }
    }

    private fun showIconDialog() {
        val dialog = requireActivity().dialog(R.layout.dialog_icon,binding.root,true)
        val recyclerView = dialog.findViewById<RecyclerView>(R.id.recy_icon)
        val adapter = IconAdapter()
        adapter.differ.submitList(createIconList())
        recyclerView.adapter = adapter

        adapter.setOnItemClick {
            icon = it.icon
            dialog.dismiss()
        }
    }


    private fun createIconList(): ArrayList<Icon> {
        val list = arrayListOf<Icon>()
        list.add(Icon(0,R.drawable.bread_1))
        list.add(Icon(1,R.drawable.heartbeat_1))
        list.add(Icon(2,R.drawable.home__2__1))
        list.add(Icon(3,R.drawable.megaphone_1))
        list.add(Icon(4,R.drawable.mortarboard_1))
        list.add(Icon(5,R.drawable.music__1__1))
        list.add(Icon(6,R.drawable.briefcase_1))
        list.add(Icon(7,R.drawable.sport_1))
        list.add(Icon(8,R.drawable.video_camera_1))
        list.add(Icon(9,R.drawable.design__1__1))
        return list
    }


    private fun createColorList(): ArrayList<CategoryColor> {
        val list = arrayListOf<CategoryColor>()
        list.add(CategoryColor(0,"#80FFD1"))
        list.add(CategoryColor(1,"#CCFF80"))
        list.add(CategoryColor(2,"#FF9680"))
        list.add(CategoryColor(3,"#80FFFF"))
        list.add(CategoryColor(4,"#80FFD9"))
        list.add(CategoryColor(5,"#809CFF"))
        list.add(CategoryColor(6,"#FC80FF"))
        list.add(CategoryColor(7,"#80FFA3"))
        list.add(CategoryColor(8,"#FFCC80"))
        list.add(CategoryColor(9,"#80D1FF"))
        return list
    }

}