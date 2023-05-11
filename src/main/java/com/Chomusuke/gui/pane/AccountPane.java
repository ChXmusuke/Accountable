/*  Accountable: a personal spending monitoring program
    Copyright (C) 2023  Artur Yukhanov

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
*/

package com.chomusuke.gui.pane;

import java.util.List;
import java.util.Map;

import com.chomusuke.logic.Storage;
import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.chomusuke.Accountable.SceneID;
import com.chomusuke.gui.element.PlusButton;
import com.chomusuke.gui.element.tile.AccountTile;
import com.chomusuke.gui.popup.AddAccountScreen;
import com.chomusuke.logic.Account;
import com.chomusuke.logic.TransactionList;
import javafx.scene.paint.Color;

import static com.chomusuke.Accountable.PADDING;

/**
 * Provides a JavaFX pane containing account information.
 */
public class AccountPane extends ContentPane {

    private final TransactionList txList;
    private final VBox accountPane;
    private final LineChart<Number, Number> chart;

    /**
     * Constructor.
     *
     * @param selectedScene the scene currently selected
     * @param txList a transaction list
     * @param balances an account map
     */
    public AccountPane(ObjectProperty<SceneID> selectedScene, TransactionList txList, Map<Byte, Account> balances) {
        this.txList = txList;

        // Controls
        HBox controls = new HBox();

        Button back = new Button("<-");



        // ----- CONTENT -----

        accountPane = new VBox();

        PlusButton addAccount = new PlusButton();

        controls.getChildren().add(back);



        // ----- CHART -----

        NumberAxis x = new NumberAxis();
        NumberAxis y = new NumberAxis();
        chart = new LineChart<>(x, y);
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        chart.getData().add(series);

        addToTop(controls, chart);

        setScrollableContent(accountPane);
        addToContent(addAccount);



        // ----- STYLE -----
        {
            accountPane.getStyleClass().add("background");
            accountPane.setPadding(new Insets(0, 0, PADDING, 0));
            accountPane.setSpacing(PADDING);
            accountPane.prefWidthProperty().bind(getContentWidthProperty());
            accountPane.prefHeightProperty().bind(getContentHeightProperty());

            addAccount.layoutXProperty().bind(getContentWidthProperty().subtract(PlusButton.RADIUS*2+PADDING));
            addAccount.layoutYProperty().bind(getContentHeightProperty().subtract(PlusButton.RADIUS*2+PADDING*2));

            // Chart
            x.setTickLabelFill(Color.LIGHTGRAY);
            y.setTickLabelFill(Color.LIGHTGRAY);
            x.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");
            y.setStyle("-fx-border-color: transparent; -fx-background-color: transparent;");

            x.setLowerBound(1);
            x.setUpperBound(12);
            x.setTickUnit(1);
            y.setForceZeroInRange(false);

            x.setAutoRanging(false);
            y.setAutoRanging(false);
            x.setMinorTickVisible(false);
            y.setMinorTickVisible(false);
            x.setTickMarkVisible(false);
            y.setTickMarkVisible(false);
            x.setMinorTickVisible(false);
            y.setMinorTickVisible(false);
            x.setTickLabelsVisible(false);

            setChartBounds();

            chart.setLegendVisible(false);
            chart.setAnimated(false);
            chart.setMaxHeight(this.getMaxHeight()*0.3);
            chart.setCreateSymbols(false);
            chart.lookup(".chart-plot-background").setStyle("-fx-background-color: transparent;");
            chart.setVerticalGridLinesVisible(false);
            chart.setHorizontalGridLinesVisible(false);
        }



        // ----- EVENTS -----
        {
            back.setOnAction(e -> selectedScene.set(SceneID.TRANSACTIONS));
            addAccount.setOnMouseClicked(e -> new AddAccountScreen(balances, txList).show());

            y.setTickLabelFormatter(new NumberAxis.DefaultFormatter(y) {
                @Override
                public String toString(Number object) {
                    if (object.doubleValue() == 0 || object.doubleValue() == series.getData().get(series.getData().size() - 1).getYValue().doubleValue())
                        return Double.toString(object.doubleValue());

                    return "";
                }
            });
        }
    }

    /**
     * Updates the display.
     *
     * @param balances an account map
     */
    public void update(Map<Byte, Account> balances) {
        accountPane.getChildren().clear();

        // Update account tiles
        for (Account a : balances.values()) {
            if (a.getBalance() >= 0) {
                AccountTile t = new AccountTile(a);

                t.setOnMouseClicked(m ->
                        new AddAccountScreen(balances, t.getBaseAccount(), txList).show()
                );

                accountPane.getChildren().add(t);
            }
        }

        // Update the chart
        XYChart.Series<Number, Number> ytdData = chart.getData().get(0);
        ytdData.getData().clear();

        double[] ytdBalances = Storage.readYTDBalances();

        for (int i = 0 ; i < ytdBalances.length ; i++) {
            XYChart.Data<Number, Number> dataPoint = new XYChart.Data<>(i+1, ytdBalances[i]);

            ytdData.getData().add(dataPoint);
        }
        List<XYChart.Data<Number, Number>> data = chart.getData().get(0).getData();

        double tick = (double) data.get(data.size()-1).getYValue();
        ((NumberAxis) chart.getYAxis()).setTickUnit(tick);

        setChartBounds();
    }

    private void setChartBounds() {
        List<XYChart.Data<Number, Number>> data = chart.getData().get(0).getData();
        double min = data.stream()
                .map(XYChart.Data::getYValue)
                .mapToDouble(Number::doubleValue)
                .min().orElse(0);
        double max = data.stream()
                .map(XYChart.Data::getYValue)
                .mapToDouble(Number::doubleValue)
                .max().orElse(0);

        min = Math.floor(min*100)/100d;
        max = Math.ceil(max*100)/100d;

        NumberAxis y = (NumberAxis) chart.getYAxis();
        y.setLowerBound(min);
        y.setUpperBound(max);
    }
}
