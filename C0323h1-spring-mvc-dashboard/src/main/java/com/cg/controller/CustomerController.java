package com.cg.controller;

import com.cg.model.Customer;
import com.cg.service.customer.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private ICustomerService customerService;

    @GetMapping()
    public String showlistpage(Model model) {
        List<Customer> customers = customerService.findAllByDeletedIsFalse();
        model.addAttribute("customers", customers);
        return "customer/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        return "customer/create";
    }

    @GetMapping("/suspended/{customerId}")
    public ModelAndView showSuspendedPage(@PathVariable Long customerId) {
        ModelAndView modelAndView = new ModelAndView("customer/suspended");
        Customer customer = customerService.findById(customerId).get();
        modelAndView.addObject("currentCustomer", customer);
        return modelAndView;
    }

    @GetMapping("/edit/{customerId}")
    public String showEditPage(@PathVariable Long customerId, Model model) {
        try {
            Optional<Customer> customerOptional = customerService.findById(customerId);

            if (!customerOptional.isPresent()) {
                return "redirect:/error.404";
            }

            Customer customer = customerOptional.get();
            model.addAttribute("customer", customer);
            return "customer/edit";
        }
        catch (Exception e) {
                return "error/404";
            }
    }




    @PostMapping("/create")
    public String doCreate(Model model, @ModelAttribute Customer customer, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            return "customer/create";
        }

        String email = customer.getEmail();
        boolean existsEmail = customerService.existsByEmail(email);

        String phone = customer.getPhone();
        boolean existsPhone = customerService.existsByPhone(phone);

        if(existsEmail) {
            model.addAttribute("notVaild", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "customer/create";
        }

        if(existsPhone) {
            model.addAttribute("notValid", true);
            model.addAttribute("message", "Phone đã tồn tại");
            return "customer/create";
        }

        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("mess", "createMess");

        return "redirect:/customers";
    }

    @PostMapping("/suspended/{customerId}")
    public String suspendCustomer(@PathVariable Long customerId, RedirectAttributes redirectAttributes) {
        Optional<Customer> optionalCustomer = customerService.findById(customerId);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            customer.setDeleted(true);
            customerService.save(customer);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Khách hàng đã bị tạm hoãn thành công");
        } else {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy khách hàng");
        }
        redirectAttributes.addFlashAttribute("mess", "suspendMess");

        return "redirect:/customers";
    }

    @PostMapping("/edit/{customerId}")
    public String doUpdate(@PathVariable Long customerId, @ModelAttribute Customer customer, Model model, RedirectAttributes redirectAttributes) {

        customer.setId(customerId);
        customerService.save(customer);

        List<Customer> customers = customerService.findAll();

        model.addAttribute("customers", customers);
        redirectAttributes.addFlashAttribute("mess", "editMess");

        return "redirect:/customers";
    }

}
