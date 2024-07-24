package com.example.usermicroservice.service;


import com.example.usermicroservice.entity.Role;
import com.example.usermicroservice.entity.User;
import com.example.usermicroservice.repository.UserRepository;
import com.example.usermicroservice.security.UserPrincipal;
import lombok.AllArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl  {
    //  private UserRepo userRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;



    public void register(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setRole(user.getRole());

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(false);

        userRepository.save(user);

        sendVerificationEmail(user, siteURL);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "ghofrane.ferchichi@edu.isetcom.tn";
        String senderName = "hello";
        String subject = "Please verify your registration";
        String content = "Hello [[name]],Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,[[name]]<br>"
;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("ghofrane.ferchichi@edu.isetcom.tn");
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstname());
        String verifyURL = siteURL + "/api/user/verify?code=" + user.getVerificationCode();
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
    public boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);

        if (user == null || user.isEnabled()) {
            return false;
        } else {
            // Set the verification code to null to indicate it's been used

            // Enable the user
            user.setEnabled(true);

            // Save the changes to the database
            userRepository.save(user);

            return true;
        }
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void makeAdmin(String email) {
        userRepository.updateUserRole(email, Role.ADMIN);
    }

    public static UserPrincipal clientAuthenticated() {
        try {
            return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        } catch (Exception e) {
            return null;
        }
    }

    public User findByUsername(String username) {
        // Retrieve user from the database based on the username
        User user = userRepository.findUserByFirstname(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return user;
    }
    public User findById(long id) throws Exception {

        UserPrincipal user = UserServiceImpl.clientAuthenticated();

        if (user == null || !user.getId().equals(id)) {
            throw new Exception();
        }
        Optional<User> obj = userRepository.findById(id);

        try {
            return obj.get();
        } catch (NoSuchElementException e) {
            throw new Exception();
        }

    }

    @Transactional
    public User update(User obj) throws Exception {
        UserPrincipal user = UserServiceImpl.clientAuthenticated();

        User cli = findById(user.getId());

        if (user == null || !user.getId().equals(cli.getId())) {
            throw new Exception();
        }

        cli.setEmail(obj.getEmail());
        cli.setFirstname(obj.getFirstname());
        cli.setPassword(passwordEncoder.encode(obj.getPassword()));
        cli.setLastname(obj.getLastname());
        cli.setAdresse(obj.getAdresse());
        cli.setPhone(obj.getPhone());

        if (userRepository.findByEmail(cli.getEmail()) == null) {
            try {
                return userRepository.save(cli);
            } catch (Exception e) {
                throw new Exception();
            }
        }
        return cli;
    }

    public void updateResetPasswordToken(String token, String email) throws Exception {
        User customer = userRepository.findByEmail(email).orElseThrow(() -> new Exception("User by id  was not found"));
        if (customer != null) {
            customer.setResetPasswordToken(token);
            userRepository.save(customer);
        } else {
            throw new Exception("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }

    public void updatePassword(User customer, String newPassword) {

        String encodedPassword = passwordEncoder.encode(newPassword);
        customer.setPassword(encodedPassword);

        customer.setResetPasswordToken(null);
        userRepository.save(customer);
    }

}

