/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebook.fx.view;

import java.awt.print.Book;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

/**
 *
 * @author maykoone
 */
public class DecimalFormattedTabelCellFactory implements Callback<TableColumn<Book, Double>, TableCell<Book, Double>> {

    private static NumberFormat format = DecimalFormat.getInstance();

    {
        format.setMaximumFractionDigits(1);
        format.setRoundingMode(RoundingMode.DOWN);
    }

    @Override
    public TableCell<Book, Double> call(TableColumn<Book, Double> param) {
        TableCell<Book, Double> cell = new TableCell<Book, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                if (item == getItem()) {
                    return;
                }

                super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
                if (item != null) {
                    setText(format.format(item));
                }

            }

        };
        cell.setTextAlignment(TextAlignment.RIGHT);
        return cell;
    }

}
