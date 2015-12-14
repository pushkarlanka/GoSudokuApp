package pushkarlanka.gosudoku;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import models.GridSolver;

public class GridActivity extends Activity {

    private static final int DIMENSION = 9;
    private TextView[][] gridItems;
    private Resources res;
    private GridSolver gridSolver;
//    private int[][] solvedGrid;
    private LayoutInflater inflater;
    private TextView selectedItem;
    private int selectedItemHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        res = getResources();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        gridItems = new TextView[DIMENSION][DIMENSION];
        selectedItem = null;

        String initialGridValues = "400000060000097000007200000005601470001008090000520000010800000006000030030902740";

        gridSolver = new GridSolver(initialGridValues);
//        solvedGrid = gridSolver.getSolution();

        setInitialGrid(initialGridValues);
        createNumberButtons();

        setOnClickListeners();

        Chronometer chronometer = (Chronometer) findViewById(R.id.chronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }

    private void setInitialGrid(String initialGridValues) {

        TableLayout grid = (TableLayout) findViewById(R.id.main_grid);

        for(int i = 0; i < DIMENSION; i++) {
            TableRow gridRow = (TableRow) inflater.inflate(R.layout.grid_row, null);
            for(int j = 0; j < DIMENSION; j++) {
                final TextView gridItem = (TextView) inflater.inflate(R.layout.grid_item, gridRow, false);
                String gridItemValue = String.valueOf(initialGridValues.charAt(i * DIMENSION + j));
                if(!gridItemValue.equals("0")) {
                    gridItem.setText(gridItemValue);
                }

                final int r = i;
                final int c = j;
                gridItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(selectedItem != null) {
                            selectedItem.setBackground(res.getDrawable(R.drawable.grid_item, getTheme()));
                        }
                        selectedItem = gridItem;
                        selectedItemHint = gridSolver.get(r, c);
                        selectedItem.setBackground(res.getDrawable(R.drawable.grid_selected_item, getTheme()));
                    }
                });

                if(!gridItemValue.equals("0")) {
                    gridItem.setTextColor(Color.parseColor("#000000"));
                    gridItem.setBackground(res.getDrawable(R.drawable.grid_filled_item, getTheme()));
                    gridItem.setEnabled(false);
                }

                gridItems[i][j] = gridItem;
                gridRow.addView(gridItem);
            }
            grid.addView(gridRow);
        }
    }

    private void createNumberButtons() {

        LinearLayout numButtonsRow = (LinearLayout) findViewById(R.id.number_buttons_row);
        for(int i = 1; i <= DIMENSION; i++) {
            final Button numButton = (Button) inflater.inflate(R.layout.num_button, numButtonsRow, false);
            numButton.setText(String.valueOf(i));
            numButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(selectedItem == null) {
                        Toast.makeText(getApplicationContext(), "Click on a tile!", Toast.LENGTH_SHORT).show();
                    } else {
                        selectedItem.setText(numButton.getText());
                    }
                }
            });
            numButtonsRow.addView(numButton);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_grid, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setOnClickListeners() {

        // changing design to calculate these in onCreate() ___ Oct 8th '15
//        final GridSolver gridSolver = new GridSolver(getCurrentGridValues());
//        final int[][] solvedGrid = gridSolver.getSolution();

        Button solveBtn = (Button) findViewById(R.id.solve_btn);
        solveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gridSolver.isSolvable()) {
                    for (int i = 0; i < DIMENSION; i++) {
                        for (int j = 0; j < DIMENSION; j++) {
                            gridItems[i][j].setText(String.valueOf(gridSolver.get(i, j)));
                        }
                    }
                }
            }
        });

        final Button hintBtn = (Button) findViewById(R.id.hint_btn);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedItem == null) {
                    Toast.makeText(getApplicationContext(), "Click on a tile!", Toast.LENGTH_SHORT).show();
                } else {
                    selectedItem.setText(String.valueOf(selectedItemHint));
                    selectedItem.setTextColor(res.getColor(R.color.hint_btn_red));
                }
            }
        });
    }

    private int[][] getCurrentGridValues() {
        int[][] currentGridValues = new int[DIMENSION][DIMENSION];
        for(int i = 0; i < DIMENSION; i++) {
            for(int j = 0; j < DIMENSION; j++) {
                currentGridValues[i][j] = Integer.parseInt(gridItems[i][j].getText().toString());
            }
        }
        return currentGridValues;
    }
}
