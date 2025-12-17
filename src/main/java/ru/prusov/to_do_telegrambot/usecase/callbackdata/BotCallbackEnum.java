package ru.prusov.to_do_telegrambot.usecase.callbackdata;

public enum BotCallbackEnum {
    FILTER_EXPERIENCE_TOGGLE ("/filterExperienceToggle"),
    MENU ("/menu");
    private final String value;

     BotCallbackEnum(String value){
        this.value = value;
    }

    public String getCallbackName() {
        return value;
    }
}
