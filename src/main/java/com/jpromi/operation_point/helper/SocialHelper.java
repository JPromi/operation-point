package com.jpromi.operation_point.helper;

import org.jspecify.annotations.Nullable;

public class SocialHelper {

    @Nullable
    public static String GetUrlFromSocialLinks(String url, String type, String name) {
        if (url != null) {
            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                return "https://" + url;
            } else {
                return url;
            }
        } else {
            return switch (type) {
                case "facebook" -> "https://www.facebook.com/" + name;
                case "instagram" -> "https://www.instagram.com/" + name;
                case "youtube" -> "https://www.youtube.com/@" + name;
                case "x" -> "https://www.x.com/" + name;
                case "tiktok" -> "https://www.tiktok.com/@" + name;
                case "flickr" -> "https://www.flickr.com/people/" + name;
                default -> null;
            };
        }
    }
}
