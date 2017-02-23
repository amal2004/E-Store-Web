package com.amal.estore.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amal.estore.beans.CategoryList;
import com.amal.estore.model.Category;
import com.amal.estore.model.service.CategoryService;

@Controller
@RequestMapping("/api/categories")
public class CategoryRestController {
	
	@Autowired
	CategoryService categoryService;

	
	@RequestMapping("/{id}")
	@ResponseBody
	public Category getCategory(@PathVariable("id") Long categoryId) {
		Category category = categoryService.findCategoryEagerly(categoryId);
		return category;
	}
	
	@RequestMapping
	@ResponseBody
	public CategoryList getCategories(@RequestParam("start") int start, @RequestParam("size") int size ) {
		List<Category> categoryEntries = categoryService.findCategoryEntries(start, size);
		return new CategoryList(categoryEntries);
		
	}
}
