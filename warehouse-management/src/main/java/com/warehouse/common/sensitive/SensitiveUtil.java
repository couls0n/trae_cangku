package com.warehouse.common.sensitive;

public class SensitiveUtil {

    public static String mask(String value, SensitiveType type) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        switch (type) {
            case PHONE:
                return maskPhone(value);
            case ID_CARD:
                return maskIdCard(value);
            case NAME:
                return maskName(value);
            case ADDRESS:
                return maskAddress(value);
            case EMAIL:
                return maskEmail(value);
            default:
                return value;
        }
    }

    private static String maskPhone(String phone) {
        if (phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    private static String maskIdCard(String idCard) {
        if (idCard.length() < 15) {
            return idCard;
        }
        return idCard.substring(0, 3) + "**********" + idCard.substring(idCard.length() - 4);
    }

    private static String maskName(String name) {
        if (name.length() <= 1) {
            return name;
        }
        if (name.length() == 2) {
            return name.substring(0, 1) + "*";
        }
        return name.substring(0, 1) + "*" + name.substring(name.length() - 1);
    }

    private static String maskAddress(String address) {
        if (address.length() <= 4) {
            return address;
        }
        return address.substring(0, 2) + "****" + address.substring(address.length() - 2);
    }

    private static String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 2) {
            return email;
        }
        return email.substring(0, 2) + "****" + email.substring(atIndex);
    }
}
