package tfar.damageenchantsmyinventory;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.enchantment.Enchantment;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.ducks.PlayerDuck;

import java.util.Collection;

public class ModCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register(Commands.literal(DamageEnchantsMyInventory.MOD_ID)
                .then(Commands.literal("hunter")
                        .then(Commands.argument("players", EntityArgument.players()).executes(ModCommands::setHunter))
                )
                .then(Commands.literal("runner")
                        .then(Commands.argument("players", EntityArgument.players()).executes(ModCommands::setRunner))
                )
                .then(Commands.literal("force")
                        .then(Commands.argument("enchantment", ResourceArgument.resource(context, Registries.ENCHANTMENT)).executes(ModCommands::forceNextRunnerEnchant)
                        )
                )
        );
    }

    static int setRunner(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        for (ServerPlayer player : players) {
            PlayerDuck.of(player).setRunner(true);
        }
        return players.size();
    }

    static int forceNextRunnerEnchant(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Holder.Reference<Enchantment> holder = ResourceArgument.getEnchantment(ctx, "enchantment");
        ModLevelData.getOrCreateDefaultInstance(ctx.getSource().getServer()).setForcedRunnerEnchantment(holder);
        return 1;
    }

    static int setHunter(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "players");
        for (ServerPlayer player : players) {
            PlayerDuck.of(player).setRunner(false);
        }
        return players.size();
    }

}
