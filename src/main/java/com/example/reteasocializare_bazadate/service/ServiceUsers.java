package com.example.reteasocializare_bazadate.service;



import com.example.reteasocializare_bazadate.model.User;
import com.example.reteasocializare_bazadate.model.validators.UserValidator;
import com.example.reteasocializare_bazadate.repository.RepoException;
import com.example.reteasocializare_bazadate.repository.RepoUsersDB;

import java.util.Set;

public class ServiceUsers {

private RepoUsersDB repoUsers;
private UserValidator userValidator;

    public ServiceUsers(RepoUsersDB repoUsers, UserValidator userValidator) {
        this.repoUsers = repoUsers;
        this.userValidator = userValidator;
    }

    /**
     * functie ce adauga in lista repoUsers un nou user
     * @param username username-ul noului cont
     * @param password parola noului cont
     */
    public void addUser(String username, String password) {
        User user =new User(username,password);
        userValidator.validate(user);
        repoUsers.addUser(user);
    }

    /**
     * functie ce sterge din lista repoUsers userul cu username ul username
     * @param username username ul de sters
     */
    public void delUser(String username) {
        User user =new User(username);
        userValidator.validate(user);
        repoUsers.delUser(user);
    }

    /**
     * functie ce verifica daca doi useri exista in repo
     * @param username1  usernameul primului user
     * @param username2 usernameul celui de al doilea user
     */
    public void verifUsers(String username1, String username2) {
        //repoUsers.verifUsers(new User(username1),new User(username2));
    }

    public Set<User> getUsers() {
        return repoUsers.getUsers();
    }

    public void loginTest(User credidentialUser) {
        if(repoUsers.find(credidentialUser)==null)
            throw new RepoException("Username-ul sau parola sunt incorecte!");

    }
}
