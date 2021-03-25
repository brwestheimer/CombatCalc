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
    private double baseCombatLevel;
    private double meleeCombatLevel;
    private double rangedCombatLevel;
    private double magicCombatLevel;
    private double combatLevel;
    private double prayerNeeded;
    private double defenceNeeded;
    private double hitpointsNeeded;
    private double attackNeeded;
    private double strengthNeeded;
    private double magicNeeded;
    private double rangeNeeded;

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

        calculatePrayerNeeded();
        calculateDefenceNeeded();
        calculateHitpointsNeeded();
        calculateAttackNeeded();
        calculateStrengthNeeded();
        calculateRangeNeeded();
        calculateMagicNeeded();

        log.info(String.valueOf(combatLevel));
    }

    private void calculateBaseCombat(){
        this.baseCombatLevel = (BASE_MULTIPLIER * ((Integer.parseInt(combatInput.getPrayerField().getText()) / 2) +
                Integer.parseInt(combatInput.getDefenceField().getText()) +
                Integer.parseInt(combatInput.getHitpointsField().getText())));
        log.info("Your base combat level is " + this.baseCombatLevel);
    }

    private void calculateMeleeCombat(){
        this.meleeCombatLevel = (COMBAT_MULTIPLIER * (Integer.parseInt(combatInput.getAttackField().getText()) +
                Integer.parseInt(combatInput.getStrengthField().getText())));
        log.info("Your melee combat level is " + this.meleeCombatLevel);
    }

    private void calculateRangedCombat(){
        this.rangedCombatLevel = (COMBAT_MULTIPLIER * (Integer.parseInt(combatInput.getRangedField().getText()) *
                RANGE_MAGE_MULTIPLIER));
        log.info("Your ranged combat level is " + this.rangedCombatLevel);
    }

    private void calculateMagicCombat(){
        this.magicCombatLevel = (COMBAT_MULTIPLIER * (Integer.parseInt(combatInput.getMagicField().getText()) *
                RANGE_MAGE_MULTIPLIER));
        log.info("Your magic combat level is " + this.magicCombatLevel);
    }

    private void calculatePrayerNeeded(){
        double nextBaseLevel = ((int)this.combatLevel + 1) - this.meleeCombatLevel;
        double nextPrayerLevel = (((nextBaseLevel * 4) - Integer.parseInt(combatInput.getDefenceField().getText())
                - Integer.parseInt(combatInput.getHitpointsField().getText())) * 2);
        this.prayerNeeded = nextPrayerLevel - Integer.parseInt(combatInput.getPrayerField().getText());

        log.info("To level up with prayer you need " + (int)this.prayerNeeded + " levels!");
    }

    private void calculateDefenceNeeded(){
        double nextBaseLevel = ((int)this.combatLevel + 1) - this.meleeCombatLevel;
        double nextDefLevel = ((nextBaseLevel * 4) - Integer.parseInt(combatInput.getHitpointsField().getText())
                - (Integer.parseInt(combatInput.getPrayerField().getText()) / 2));
        this.defenceNeeded = nextDefLevel - Integer.parseInt(combatInput.getDefenceField().getText());
        log.info("To level up with defence you need " + (int)this.defenceNeeded + " levels!");
    }

    private void calculateHitpointsNeeded(){
        double nextBaseLevel = ((int)this.combatLevel + 1) - this.meleeCombatLevel;
        double nextHPLevel = ((nextBaseLevel * 4) - Integer.parseInt(combatInput.getDefenceField().getText())
                - (Integer.parseInt(combatInput.getPrayerField().getText()) / 2));
        this.hitpointsNeeded = nextHPLevel - Integer.parseInt(combatInput.getHitpointsField().getText());
        log.info("To level up with hitpoints you need " + (int)this.hitpointsNeeded + " levels!");
    }

    private void calculateAttackNeeded(){
        double nextMeleeLevel = ((int) this.combatLevel +1) - this.baseCombatLevel;
        double nextAttackLevel = ((nextMeleeLevel / this.COMBAT_MULTIPLIER)
                - Integer.parseInt(combatInput.getStrengthField().getText()));
        this.attackNeeded = nextAttackLevel - Integer.parseInt(combatInput.getAttackField().getText());
        log.info("To level up with attack you need " + (int)this.attackNeeded + " levels!");
    }

    private void calculateStrengthNeeded(){
        double nextMeleeLevel = ((int) this.combatLevel +1) - this.baseCombatLevel;
        double nextStrLevel = ((nextMeleeLevel / this.COMBAT_MULTIPLIER)
                - Integer.parseInt(combatInput.getAttackField().getText()));
        this.strengthNeeded = nextStrLevel - Integer.parseInt(combatInput.getStrengthField().getText());
        log.info("To level up with strength you need " + (int)this.strengthNeeded + " levels!");
    }

    private void calculateMagicNeeded(){
        double nextMagicCBLevel = ((int) this.combatLevel + 1) - this.baseCombatLevel;
        double nextMagicLevel = ((nextMagicCBLevel / COMBAT_MULTIPLIER) / RANGE_MAGE_MULTIPLIER);
        this.magicNeeded = nextMagicLevel - Integer.parseInt(combatInput.getMagicField().getText());
        log.info("To level up with magic you need " + (int)this.magicNeeded + " levels!");
    }

    private void calculateRangeNeeded(){
        double nextMagicCBLevel = ((int) this.combatLevel + 1) - this.baseCombatLevel;
        double nextMagicLevel = ((nextMagicCBLevel / COMBAT_MULTIPLIER) / RANGE_MAGE_MULTIPLIER);
        this.rangeNeeded = nextMagicLevel - Integer.parseInt(combatInput.getRangedField().getText());
        log.info("To level up with range you need " + (int)this.rangeNeeded + " levels!");
    }

    public void updateOutput(){
        this.combatOutput.setCombatValue((int)combatLevel);
    }
}
