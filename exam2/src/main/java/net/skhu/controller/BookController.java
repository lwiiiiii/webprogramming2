package net.skhu.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.skhu.dto.Book;
import net.skhu.dto.Category;
import net.skhu.dto.Publisher;
import net.skhu.mapper.BookMapper;
import net.skhu.mapper.CategoryMapper;
import net.skhu.mapper.PublisherMapper;

@Controller
@RequestMapping("/book")
public class BookController {
	@Autowired BookMapper bookMapper;
	@Autowired CategoryMapper categoryMapper;
	@Autowired PublisherMapper publisherMapper;

	@RequestMapping(value="list1", method=RequestMethod.GET)
	public String list1(Model model) {
		List<Book> books = bookMapper.findAll();
		model.addAttribute("books", books);
		return "book/list1";
	}

	@RequestMapping(value="list2", method=RequestMethod.GET)
	public String list2(Model model) {
		model.addAttribute("books", bookMapper.findAllWithCategoryAndPublisher());
		return "book/list2";
	}

	@RequestMapping(value="create", method=RequestMethod.GET)
	public String create(Model model) {
		Book book = new Book();
		List<Category> categorys = categoryMapper.findAll();
		List<Publisher> publishers = publisherMapper.findAll();
		model.addAttribute("book", book);
		model.addAttribute("categorys", categorys);
		model.addAttribute("publishers", publishers);
		return "book/create";
	}

	@RequestMapping(value="create", method=RequestMethod.POST)
	public String create(Model model, Book book) {
		bookMapper.insert(book);
		return "redirect:list1";
	}

	@RequestMapping(value="edit", method=RequestMethod.GET)
	public String edit(Model model, @RequestParam("id") int id) {
		Book book = bookMapper.findOne(id);
		List<Category> categorys = categoryMapper.findAll();
		List<Publisher> publishers = publisherMapper.findAll();
		model.addAttribute("book", book);
		model.addAttribute("categorys", categorys);
		model.addAttribute("publishers", publishers);
		return "book/edit";
	}

	@RequestMapping(value="edit", method=RequestMethod.POST)
	public String edit(Model model, Book book) {
		bookMapper.update(book);
		return "redirect:list1";
	}

	 @RequestMapping("delete")
	    public String delete(Model model, @RequestParam("id") int id) {
	        bookMapper.delete(id);
	        return "redirect:list1";
	    }
}
