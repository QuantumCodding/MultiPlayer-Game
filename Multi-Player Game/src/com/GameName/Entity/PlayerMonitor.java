package com.GameName.Entity;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.Box;
import javax.swing.JLabel;

import com.GameName.Main.Debugging.DebugPanel;
import com.GameName.Util.Vectors.Vector3f;
import com.GameName.World.World;

public class PlayerMonitor extends DebugPanel {	
	private static final long serialVersionUID = 1L;
	
	private JLabel nameLabel;
	private JLabel healthLabel;
	private JLabel positionLabel;
	private JLabel manaLabel;
	private JLabel chunkLabel;
	private JLabel hungerLabel;
	private JLabel selectedLabel;

	public PlayerMonitor() {
		super("Player Monitor");
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		nameLabel = new JLabel("Name: ");
		nameLabel.setFont(new Font("Tahoma", Font.PLAIN, 21));
		GridBagConstraints gbc_nameLabel = new GridBagConstraints();
		gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nameLabel.gridx = 1;
		gbc_nameLabel.gridy = 0;
		add(nameLabel, gbc_nameLabel);
		
		Component verticalStrut = Box.createVerticalStrut(10);
		GridBagConstraints gbc_verticalStrut = new GridBagConstraints();
		gbc_verticalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_verticalStrut.gridx = 1;
		gbc_verticalStrut.gridy = 1;
		add(verticalStrut, gbc_verticalStrut);
		
		healthLabel = new JLabel("Health: ");
		healthLabel.setForeground(new Color(255, 0, 0));
		healthLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_healthLabel = new GridBagConstraints();
		gbc_healthLabel.insets = new Insets(0, 0, 5, 5);
		gbc_healthLabel.gridx = 1;
		gbc_healthLabel.gridy = 2;
		add(healthLabel, gbc_healthLabel);
		
		positionLabel = new JLabel("Position:");
		positionLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_positionLabel = new GridBagConstraints();
		gbc_positionLabel.insets = new Insets(0, 0, 5, 0);
		gbc_positionLabel.gridx = 8;
		gbc_positionLabel.gridy = 2;
		add(positionLabel, gbc_positionLabel);
		
		manaLabel = new JLabel("Mana: ");
		manaLabel.setForeground(new Color(32, 178, 170));
		manaLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_manaLabel = new GridBagConstraints();
		gbc_manaLabel.insets = new Insets(0, 0, 5, 5);
		gbc_manaLabel.gridx = 1;
		gbc_manaLabel.gridy = 3;
		add(manaLabel, gbc_manaLabel);
		
		chunkLabel = new JLabel("Chunk:");
		chunkLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_chunkLabel = new GridBagConstraints();
		gbc_chunkLabel.insets = new Insets(0, 0, 5, 0);
		gbc_chunkLabel.gridx = 8;
		gbc_chunkLabel.gridy = 3;
		add(chunkLabel, gbc_chunkLabel);
		
		hungerLabel = new JLabel("Hunger:");
		hungerLabel.setForeground(new Color(205, 133, 63));
		hungerLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_hungerLabel = new GridBagConstraints();
		gbc_hungerLabel.insets = new Insets(0, 0, 5, 5);
		gbc_hungerLabel.gridx = 1;
		gbc_hungerLabel.gridy = 4;
		add(hungerLabel, gbc_hungerLabel);
		
		selectedLabel = new JLabel("Selected: ");
		selectedLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_selectedLabel = new GridBagConstraints();
		gbc_selectedLabel.insets = new Insets(0, 0, 5, 0);
		gbc_selectedLabel.gridx = 8;
		gbc_selectedLabel.gridy = 4;
		add(selectedLabel, gbc_selectedLabel);
	}

	public void tick(EntityPlayer player) {
		healthLabel.setText("Health: " + player.getAccess().getHealth() + " / " + player.getAccess().getMaxHealth());
		manaLabel.setText("Mana: " + player.getAccess().getMana() + " / " + player.getAccess().getMaxMana());
		hungerLabel.setText("Hunger: " + player.getAccess().getHunger() + " / " + player.getAccess().getMaxHunger());
		
		float chunkCoordX = player.getAccess().getPos().getX() / World.CHUNK_SIZE;
		float chunkCoordY = player.getAccess().getPos().getY() / World.CHUNK_SIZE;
		float chunkCoordZ = player.getAccess().getPos().getZ() / World.CHUNK_SIZE;
		
		positionLabel.setText("Pos: " + player.getAccess().getPos().truncate().valuesToString());
		chunkLabel.setText("Chunk: " + new Vector3f(chunkCoordX, chunkCoordY, chunkCoordZ).truncate().valuesToString());
		selectedLabel.setText("Slected: " + player.getAccess().getSelectedCube().valuesToString());
	}
}
