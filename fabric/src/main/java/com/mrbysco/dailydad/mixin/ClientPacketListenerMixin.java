package com.mrbysco.dailydad.mixin;

import com.mrbysco.dailydad.callback.ClientEventsCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin extends ClientCommonPacketListenerImpl {

	protected ClientPacketListenerMixin(Minecraft minecraft, Connection connection, CommonListenerCookie commonListenerCookie) {
		super(minecraft, connection, commonListenerCookie);
	}

	@Unique
	private LocalPlayer tmpPlayer;

	@Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/chat/MessageSignatureCache;createDefault()Lnet/minecraft/network/chat/MessageSignatureCache;"))
	private void handleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
		ClientEventsCallback.LOGIN_EVENT.invoker().onLogin(minecraft.player);
	}

	@Inject(method = "handleRespawn", at = @At("HEAD"))
	private void handleRespawnPre(ClientboundRespawnPacket packet, CallbackInfo ci) {
		this.tmpPlayer = minecraft.player;
	}

	@Inject(method = "handleRespawn", at = @At(value = "INVOKE",
			target = "Lnet/minecraft/client/multiplayer/ClientLevel;addEntity(Lnet/minecraft/world/entity/Entity;)V"))
	private void handleRespawn(ClientboundRespawnPacket packet, CallbackInfo ci) {
		ClientEventsCallback.RESPAWN_EVENT.invoker().onClientRespawn(tmpPlayer, minecraft.player);
		this.tmpPlayer = null;
	}
}