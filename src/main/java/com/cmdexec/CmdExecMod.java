package com.cmdexec;

import com.cmdexec.util.BashUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CmdExecMod implements ModInitializer {
    public static final String MOD_ID = "cmdexec";
    // 指令前缀 !bash
    private static final String PREFIX = "!bash ";

    @Override
    public void onInitialize() {
        // 监听玩家聊天消息
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            if (!(sender instanceof ServerPlayerEntity player)) return;

            String msg = message.getContent().getString().trim();
            // 判断是否是执行命令
            if (msg.startsWith(PREFIX)) {
                // 取消原消息发送
                return false;
            }
            return true;
        });

        // 监听聊天文本（拦截处理）
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, player, params) -> {
            String content = message.getContent().getString().trim();
            if (content.startsWith(PREFIX)) {
                // 截取真实命令
                String realCmd = content.substring(PREFIX.length());
                // 执行bash
                String output = BashUtil.runBash(realCmd);
                // 分段发送结果（防止超长）
                player.sendMessage(Text.literal("§6【Bash执行结果】"), false);
                for (String s : output.split("\n")) {
                    if (!s.isBlank()) player.sendMessage(Text.literal("§7" + s), false);
                }
                return false;
            }
            return true;
        });
    }
}

