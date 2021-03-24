package com.combatcalc;

import lombok.Value;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import lombok.extern.slf4j.Slf4j;
import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.*;

@Slf4j
public class CombatCalcOutputPanel extends JPanel {

    private JLabel combatLevelValue;
    private JLabel nextLevelValue;
    private JLabel nextAttackValue;
    private JLabel nextDefenseValue;
    private JLabel nextHitpointsValue;
    private JLabel nextRangedValue;
    private JLabel nextMagicValue;
    private JLabel nextPrayerValue;
    private JLabel nextStrengthValue;
    private GridBagConstraints c;

    CombatCalcOutputPanel(){

        setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 0;

        JPanel tempContainer = new JPanel();
        tempContainer.setLayout(new BorderLayout());
        tempContainer.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        JLabel combatLevelText = new JLabel("Combat Level: ");
        combatLevelText.setFont(FontManager.getRunescapeBoldFont());
        combatLevelValue = new JLabel();
        combatLevelValue.setForeground(Color.GREEN);
        combatLevelValue.setFont(FontManager.getRunescapeBoldFont());
        tempContainer.add(combatLevelText,BorderLayout.WEST);
        tempContainer.add(combatLevelValue,BorderLayout.CENTER);
        add(tempContainer,c);
        c.gridy++;

        tempContainer = new JPanel();
        tempContainer.setLayout(new BorderLayout());
        tempContainer.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);
        JLabel messageLabelBeginning = new JLabel("Levels needed for ");
        messageLabelBeginning.setFont(FontManager.getRunescapeFont());
        nextLevelValue = new JLabel();
        nextLevelValue.setForeground(Color.GREEN);
        nextLevelValue.setFont(FontManager.getRunescapeBoldFont());
        tempContainer.add(messageLabelBeginning,BorderLayout.WEST);
        tempContainer.add(nextLevelValue,BorderLayout.CENTER);
        add(tempContainer,c);
        c.gridy++;

        nextAttackValue = addNextLevelComponent("Attack");
        nextStrengthValue = addNextLevelComponent("Strength");
        nextDefenseValue = addNextLevelComponent("Defense");
        nextRangedValue = addNextLevelComponent("Ranged");
        nextMagicValue = addNextLevelComponent("Magic");
        nextPrayerValue = addNextLevelComponent("Prayer");
        nextHitpointsValue = addNextLevelComponent("Hitpoints");
    }

    private JLabel addNextLevelComponent(String label){
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        container.setBackground(ColorScheme.MEDIUM_GRAY_COLOR);

        final JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(FontManager.getRunescapeFont());

        final JLabel valueLabel = new JLabel();
        valueLabel.setFont(FontManager.getRunescapeBoldFont());
        valueLabel.setForeground(Color.GREEN);

        container.add(fieldLabel,BorderLayout.CENTER);
        container.add(valueLabel,BorderLayout.WEST);

        add(container,c);
        c.gridy++;

        return valueLabel;
    }

    public void setCombatValue(int combatLevel){
        this.combatLevelValue.setText(String.valueOf(combatLevel));
        //since the next level is always combat + 1
        this.nextLevelValue.setText(String.valueOf(combatLevel + 1));
    }
}
