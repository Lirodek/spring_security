package com.security.demosecurity.common;

import com.security.demosecurity.account.Account;
import com.security.demosecurity.account.AccountService;
import com.security.demosecurity.book.Book;
import com.security.demosecurity.book.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultDataGenerator implements ApplicationRunner {

    @Autowired
    AccountService accountService;

    @Autowired
    BookRepository bookRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Account lirodek = createUser("lirodek");
        Account gwngmin = createUser("gwngmin");

        createBook("spring",lirodek);
        createBook("hibernate",gwngmin);

    }

    private void createBook(String title, Account lirodek) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(lirodek);
        bookRepository.save(book);
    }

    private Account createUser(String username) {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword("123");
        account.setRole("USER");

        return accountService.createNew(account);
    }
}
