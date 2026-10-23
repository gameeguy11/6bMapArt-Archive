package com.maparts.link.util;

import java.util.Locale;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;

/**
 * Uploads feed the 6b6t community's gallery, so /mapupload and
 * /mapuploadall refuse to run anywhere else. The address compared is the
 * one the player typed in the multiplayer menu: entries below match both
 * the bare domain and every subdomain, so all 6b6t alts (play., alt1.,
 * alt2., …) qualify without a mod update — servers reached another way
 * (a direct IP or an unlisted domain) have to be listed once confirmed.
 */
public final class ServerAllowlist {
    private static final String[] DOMAINS = {
            "6b6t.org",
            // 8b8t.me and other confirmed 6b6t addresses go here.
    };

    private ServerAllowlist() {}

    /**
     * The connected server's host (lowercase, port stripped) when it is a
     * known 6b6t address; null when singleplayer, in a menu, or on any
     * other server.
     */
    public static String connected6b6tHost(Minecraft client) {
        ServerData server = client.getCurrentServer();

        if (server == null || server.ip == null || server.ip.isBlank()) {
            return null;
        }

        String host = normalize(server.ip);

        for (String domain : DOMAINS) {
            if (host.equals(domain) || host.endsWith("." + domain)) {
                return host;
            }
        }

        return null;
    }

    private static String normalize(String address) {
        String host = address.trim().toLowerCase(Locale.ROOT);
        int colon = host.lastIndexOf(':');

        // Strip an explicit :port; leave IPv6 literals like [::1] alone.
        if (colon > 0 && host.indexOf(':') == colon) {
            host = host.substring(0, colon);
        }

        return host;
    }
}
