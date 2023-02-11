package me.darki.konas.util.client;

import com.google.common.base.Charsets;
import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.util.UUIDTypeAdapter;
import me.darki.konas.mixin.mixins.IMinecraft;
import me.darki.konas.util.network.APIUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.util.AbstractMap;
import java.util.UUID;

public class LoginUtils {

    public static final YggdrasilAuthenticationService loginService;
    private static final YggdrasilUserAuthentication userService;
    private static final YggdrasilMinecraftSessionService sessionService;

    static {
        loginService = new YggdrasilAuthenticationService(Minecraft.getMinecraft().getProxy(), UUID.randomUUID().toString());
        userService = (YggdrasilUserAuthentication) loginService.createUserAuthentication(Agent.MINECRAFT);
        sessionService = (YggdrasilMinecraftSessionService) loginService.createMinecraftSessionService();
    }

    public static boolean sessionValid() {
        try {
            GameProfile gp = Minecraft.getMinecraft().getSession().getProfile();
            String token = Minecraft.getMinecraft().getSession().getToken();
            String id = UUID.randomUUID().toString();

            LoginUtils.sessionService.joinServer(gp, token, id);
            if (LoginUtils.sessionService.hasJoinedServer(gp, id, null).isComplete()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
