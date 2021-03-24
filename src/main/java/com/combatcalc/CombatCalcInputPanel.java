package com.combatcalc;

import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.components.FlatTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

@Getter
public class CombatCalcInputPanel extends JPanel {

    private final JTextField attackField;
    private final JTextField strengthField;
    private final JTextField defenceField;
    private final JTextField rangedField;
    private final JTextField magicField;
    private final JTextField prayerField;
    private final JTextField hitpointsField;

    CombatCalcInputPanel(){
        setLayout(new GridLayout(4, 2, 7, 7));
        attackField = addComponent("Attack");
        strengthField = addComponent("Strength");
        defenceField = addComponent("Defence");
        rangedField = addComponent("Ranged");
        magicField = addComponent("Magic");
        prayerField = addComponent("Prayer");
        hitpointsField = addComponent("Hitpoints");
    }

    private int getInput(JTextField field)
    {
        try
        {
            //get only integers as input
            return Integer.parseInt(field.getText().replaceAll("\\D", ""));
        }
        catch (NumberFormatException e)
        {
            //return -1 because 0 is valid input
            return -1;
        }
    }

    public void setInputField(JTextField field, int value){
        field.setText(String.valueOf(value));
    }

    private JTextField addComponent(String label)
    {
        final JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        final JLabel fieldLabel = new JLabel(label);
        final FlatTextField inputField = new FlatTextField();

        inputField.setBackground(ColorScheme.DARKER_GRAY_COLOR);
        inputField.setHoverBackgroundColor(ColorScheme.DARK_GRAY_HOVER_COLOR);
        inputField.setBorder(new EmptyBorder(5, 7, 5, 7));

        fieldLabel.setFont(FontManager.getRunescapeSmallFont());
        fieldLabel.setBorder(new EmptyBorder(0, 0, 4, 0));
        fieldLabel.setForeground(Color.WHITE);

        container.add(fieldLabel, BorderLayout.NORTH);
        container.add(inputField, BorderLayout.CENTER);

        add(container);

        return inputField.getTextField();
    }
}
