package de.techfak.gse.mbaig;

import javafx.scene.control.Button;
import javafx.scene.control.TableCell;

/*
* since we do not have a VoteButton, let's creat a custom class for it & fill our column with it
* ref: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/TableCell.html
*
 */
public class VoteCell extends TableCell<Song, String> {

    final double maxHEIGHT = 100;
    final double maxWIDTH = 100;
    private Button cellButton;

    // struct
    VoteCell() {
        cellButton = new Button("Vote");
        cellButton.setOnAction(event -> {
            GSERadioGUI.addVote(getIndex());
        });
        // scale the button
        cellButton.setMaxHeight(maxHEIGHT);
        cellButton.setMaxWidth(maxWIDTH);
        setGraphic(cellButton);
    }
}
