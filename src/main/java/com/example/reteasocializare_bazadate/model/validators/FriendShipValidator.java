package com.example.reteasocializare_bazadate.model.validators;


import com.example.reteasocializare_bazadate.model.FriendShip;

public class FriendShipValidator implements Validator<FriendShip> {

    /**
     * functie ce valideaza un friendship
     *
     * @param entity de validat
     * @throws ValidationException daca userul nu e valid
     */
    @Override
    public void validate(FriendShip entity) throws ValidationException {
        if (entity.getUsername1() == "" || entity.getUsername2() == "")
            throw new ValidationException("Username1 and Username2 is not allowed to be empty");
        if (entity.getUsername1() == entity.getUsername2())
            throw new ValidationException("Usernames need to be diferrent!");
    }
}
