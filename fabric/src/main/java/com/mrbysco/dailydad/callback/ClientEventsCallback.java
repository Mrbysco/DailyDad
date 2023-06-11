package com.mrbysco.dailydad.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

public interface ClientEventsCallback {
	Event<ClientLogin> LOGIN_EVENT = EventFactory.createArrayBacked(ClientLogin.class,
			(listeners) -> (player) -> {
				for (ClientLogin event : listeners) {
					InteractionResult result = event.onLogin(player);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);

	interface ClientLogin {
		InteractionResult onLogin(Player player);
	}

	Event<ClientRespawn> RESPAWN_EVENT = EventFactory.createArrayBacked(ClientRespawn.class,
			(listeners) -> (oldPlayer, newPlayer) -> {
				for (ClientRespawn event : listeners) {
					InteractionResult result = event.onClientRespawn(oldPlayer, newPlayer);

					if (result != InteractionResult.PASS) {
						return result;
					}
				}

				return InteractionResult.PASS;
			}
	);

	interface ClientRespawn {
		InteractionResult onClientRespawn(Player oldPlayer, Player newPlayer);
	}
}
