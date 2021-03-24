package com.combatcalc;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Skill;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.PluginPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

@Slf4j
public class CombatCalcPanel extends PluginPanel {

    private CombatCalcInputPanel combatInput;
    private CombatCalcOutputPanel combatOutput;
    private Client client;
    private final double COMBAT_MULTIPLIER = .325;
    private final double RANGE_MAGE_MULTIPLIER = 1.5;
    private final double BASE_MULTIPLIER = .25;
    private int baseCombatLevel;
    private int meleeCombatLevel;
    private int rangedCombatLevel;
    private int magicCombatLevel;
    private int combatLevel;

    CombatCalcPanel(Client client){
        this.client = client;
        //super();
        //main panel setup
        getScrollPane().setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setBorder(new EmptyBorder(10, 10, 10, 10));
        //GridBag holds both the input and output panels
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1;
        c.gridx = 0;
        c.gridy = 0;

        //input panel setup
        combatInput = new CombatCalcInputPanel();
        combatInput.setBorder(new EmptyBorder(15, 0, 15, 0));
        combatInput.setBackground(ColorScheme.DARK_GRAY_COLOR);

        add(combatInput,c);
        c.gridy++;

        //output panel setup
        combatOutput = new CombatCalcOutputPanel();
        combatOutput.setBorder(new EmptyBorder(15, 0, 15, 0));
        combatOutput.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        add(combatOutput,c);
        c.gridy++;
    }

    public void initializeInputFields(){
            combatInput.setInputField(combatInput.getAttackField(), client.getRealSkillLevel(Skill.ATTACK));
            combatInput.setInputField(combatInput.getDefenceField(), client.getRealSkillLevel(Skill.DEFENCE));
            combatInput.setInputField(combatInput.getHitpointsField(), client.getRealSkillLevel(Skill.HITPOINTS));
            combatInput.setInputField(combatInput.getStrengthField(), client.getRealSkillLevel(Skill.STRENGTH));
            combatInput.setInputField(combatInput.getRangedField(), client.getRealSkillLevel(Skill.RANGED));
            combatInput.setInputField(combatInput.getMagicField(), client.getRealSkillLevel(Skill.MAGIC));
            combatInput.setInputField(combatInput.getPrayerField(), client.getRealSkillLevel(Skill.PRAYER));
    }

    public void calculateOutputValues(){
        calculateBaseCombat();
        calculateMeleeCombat();
        calculateRangedCombat();
        calculateMagicCombat();

        //Combat level = base combat level + highest ranged/magic/melee combat level
        if(this.meleeCombatLevel > this.rangedCombatLevel && this.meleeCombatLevel > this.magicCombatLevel){
            combatLevel = meleeCombatLevel + baseCombatLevel;
        }else if(this.rangedCombatLevel > this.magicCombatLevel){
            combatLevel = rangedCombatLevel + baseCombatLevel;
        }else{
            combatLevel = magicCombatLevel +baseCombatLevel;
        }
        log.info(String.valueOf(combatLevel));
    }

    private void calculateBaseCombat(){
        this.baseCombatLevel = (int)(BASE_MULTIPLIER * ((Integer.parseInt(combatInput.getPrayerField().getText()) / 2) +
                Integer.parseInt(combatInput.getDefenceField().getText()) +
                Integer.parseInt(combatInput.getHitpointsField().getText())));
        log.info("Your base combat level is " + this.baseCombatLevel);
    }

    private void calculateMeleeCombat(){
        this.meleeCombatLevel = (int)(COMBAT_MULTIPLIER * (Integer.parseInt(combatInput.getAttackField().getText()) +
                Integer.parseInt(combatInput.getStrengthField().getText())));
        log.info("Your melee combat level is " + this.meleeCombatLevel);
    }

    private void calculateRangedCombat(){
        this.rangedCombatLevel = (int)(COMBAT_MULTIPLIER * (Integer.parseInt(combatInput.getRangedField().getText()) *
                RANGE_MAGE_MULTIPLIER));
        log.info("Your ranged combat level is " + this.rangedCombatLevel);
    }

    private void calculateMagicCombat(){
        this.magicCombatLevel = (int)(COMBAT_MULTIPLIER * (Integer.parseInt(combatInput.getMagicField().getText()) *
                RANGE_MAGE_MULTIPLIER));
        log.info("Your magic combat level is " + this.magicCombatLevel);
    }

    public void updateOutput(){
        this.combatOutput.setCombatValue(combatLevel);
    }
}
