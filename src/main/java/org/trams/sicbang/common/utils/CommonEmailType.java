package org.trams.sicbang.common.utils;

import org.trams.sicbang.model.entity.User;

import java.util.List;

/**
 * Created by KienNT on 15/09/2016.
 */
public class CommonEmailType {
    public static String toStringEmails(List<User> users){
        String emails = "";
        for(int i = 0 ; i < users.size() ; i++){
            emails+=users.get(i).getEmail();
            if(i != users.size() - 1){
                emails+=",";
            }
        }
        System.out.println("emails: "+emails);
        return emails;
    }
}
