package com.combatcalc;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;

@Slf4j
@PluginDescriptor(
	name = "Combat Level Calculator"
)
public class CombatCalcPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CombatCalcConfig config;

	@Inject
	private ClientToolbar clientToolbar;

	private NavigationButton navButton;
	private CombatCalcPanel combatPanel;
	private boolean initializationFlag = false;

	@Override
	protected void startUp() throws Exception
	{
		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), "/skill_icons/combat.png");
		combatPanel = new CombatCalcPanel(client);
		navButton = NavigationButton.builder()
				.tooltip("Combat Level Calculator")
				.icon(icon)
				.priority(3)
				.panel(combatPanel)
				.build();

		clientToolbar.addNavigation(navButton);
	}

	@Override
	protected void shutDown() throws Exception
	{
		log.info("Combat Calc stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			//Can't get skill data until after login, sets flag to retrieve data on next tick
			initializationFlag = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if(initializationFlag == true){
			initializationFlag = false;
			combatPanel.initializeInputFields();
			combatPanel.calculateOutputValues();
			combatPanel.updateOutput();
		}
	}

	@Provides
	CombatCalcConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CombatCalcConfig.class);
	}
}
