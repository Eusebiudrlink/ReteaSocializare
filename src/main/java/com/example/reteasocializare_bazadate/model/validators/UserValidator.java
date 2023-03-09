package com.example.reteasocializare_bazadate.model.validators;


import com.example.reteasocializare_bazadate.model.User;

public class UserValidator implements Validator<User> {
    /**
     * functie ce valideaza un user
     *
     * @param user user de validat
     * @throws ValidationException daca userul nu e valid
     */
    @Override
    public void validate(User user) throws ValidationException {
        if (user.getUsername() == "" || user.getPassword() == "")
            throw new ValidationException("Username or password invalid!");
    }
}
