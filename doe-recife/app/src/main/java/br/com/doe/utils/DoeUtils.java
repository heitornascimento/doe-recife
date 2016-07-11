package br.com.doe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import org.apache.commons.codec.net.URLCodec;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.doe.R;
import br.com.doe.model.Donation;
import br.com.doe.model.User;
import br.com.doe.view.DrawerItem;

/**
 * Created by heitornascimento on 4/14/16.
 */
public class DoeUtils {

    public static final String USER_EXTRA = "user";

    public static final String CURRENT_USER_SP = "current_user";
    public static final String TOKEN_SP = "token";
    public static final String USER_ID = "user_id";


    /**
     * Check if the email input is valid for registering.
     *
     * @param email The value input
     * @return Return true if the email has valid form.
     */
    public static boolean isValidEmail(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * Convert a raw list of donation into a list of Drawitem
     *
     * @param list
     * @param ctx
     * @return
     * @throws Exception
     */
    public static List<DrawerItem> parserUserData(List<Donation> list, Context ctx) throws Exception {

        List<DrawerItem> parsedList = new ArrayList<DrawerItem>();
        DrawerItem drawerItem;
        int imageViewId;
        HashMap<String, Integer> donationTypeMap = new HashMap<String, Integer>();
        HashMap<String, Integer> donationCountMap = new HashMap<String, Integer>();


        for (Donation donation : list) {

            String donationType = donation.getDonation_description();

            if (!donationCountMap.containsKey(donationType)) {
                imageViewId = getDonationIcon(donationType, ctx);
                donationTypeMap.put(donationType, imageViewId);
                donationCountMap.put(donationType, 1);
            } else {
                Integer count = donationCountMap.get(donationType);
                //update reference
                donationCountMap.put(donationType, ++count);
            }
        }

        Set<String> typeSet = donationTypeMap.keySet();
        int count;
        for (String type : typeSet) {
            drawerItem = new DrawerItem();
            drawerItem.setType(type);
            drawerItem.setIcon(donationTypeMap.get(type));
            drawerItem.setCount(String.valueOf(donationCountMap.get(type)));
            parsedList.add(drawerItem);
        }

        return parsedList;
    }

    private static int getDonationIcon(String donationType, Context ctx) throws Exception {

        String type = URLDecoder.decode(donationType, "UTF-8");
        Resources res = ctx.getResources();

        if (res.getString(R.string.material_limpeza).equals(donationType)) {
            return R.drawable.ic_limpeza_blue;
        } else if (res.getString(R.string.cesta_basica).equals(donationType)) {
            return R.drawable.ic_cesta_blue;
        } else if (res.getString(R.string.material_pedagogico).equals(donationType)) {
            return R.drawable.ic_pedagogico_blue;
        } else if (res.getString(R.string.toalha_mesa).equals(donationType)) {
            return R.drawable.ic_mesa_blue;
        } else if (res.getString(R.string.brinquedos).equals(donationType)) {
            return R.drawable.ic_brinquedos_blue;
        } else if (res.getString(R.string.toalha_banho).equals(donationType)) {
            return R.drawable.ic_banho_blue;
        } else if (res.getString(R.string.lencol).equals(donationType)) {
            return R.drawable.ic_lencol_blue;
        } else if (res.getString(R.string.remedio).equals(donationType)) {
            return R.drawable.ic_remedios_blue;
        } else if (res.getString(R.string.roupas).equals(donationType)) {
            return R.drawable.ic_roupa_blue;
        } else if (res.getString(R.string.recursos_financeiros).equals(donationType)) {
            return R.drawable.ic_financeiro_blue;
        } else if (res.getString(R.string.higiene_pessoal).equals(donationType)) {
            return R.drawable.ic_higiene_blue;
        } else if (res.getString(R.string.outros).equals(donationType)) {
            return R.drawable.ic_outros_blue;
        }

        throw new Exception("Image Not Found");
    }


    public static void saveUserData(Context ctx, User user) {

        if (user.getAuthToken() != null && !user.getAuthToken().equals("")) {
            SharedPreferences sharedPreferences = ctx.getSharedPreferences(DoeUtils.CURRENT_USER_SP, ctx.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString(DoeUtils.TOKEN_SP, user.getAuthToken());
            editor.putInt(DoeUtils.USER_ID, user.getmId());
            editor.commit();
        }
    }
}
