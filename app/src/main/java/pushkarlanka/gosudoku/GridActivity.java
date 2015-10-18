package pushkarlanka.gosudoku;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import models.GridSolver;

public class GridActivity extends Activity {

    private static final int DIMENSION = 9;
    private EditText[][] gridItems;
    private Resources res;
    private boolean hintBtnClicked = false;
    private GridSolver gridSolver;
    private int[][] solvedGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        res = getResources();

        gridItems = new EditText[DIMENSION][DIMENSION];

        String initialGridValues = "400000060000097000007200000005601470001008090000520000010800000006000030030902740";

        gridSolver = new GridSolver(initialGridValues);
        solvedGrid = gridSolver.getSolution();

        setInitialGrid(initialGridValues);

        setOnClickListeners();
    }

    private void setInitialGrid(String initialGridValues) {

        TableLayout grid = (TableLayout) findViewById(R.id.main_grid);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i = 0; i < DIMENSION; i++) {
            TableRow gridRow = (TableRow) inflater.inflate(R.layout.grid_row,null);
            for(int j = 0; j < DIMENSION; j++) {
                final EditText gridItem = (EditText) inflater.inflate(R.layout.grid_item, null);
                String gridItemValue = String.valueOf(initialGridValues.charAt(i * DIMENSION + j));
                gridItem.setText(gridItemValue);

                final int r = i;
                final int c = j;
                gridItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(hintBtnClicked) {
                            gridItem.setEnabled(false);
                            gridItem.setText(String.valueOf(solvedGrid[r][c]));
                            gridItem.setTextColor(res.getColor(R.color.hint_btn_red));
                            setHintBtnToDefault();
                        }
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
                            gridItems[i][j].setText(String.valueOf(solvedGrid[i][j]));
                        }
                    }
                }
            }
        });

        final Button hintBtn = (Button) findViewById(R.id.hint_btn);
        hintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!hintBtnClicked) {
                    hintBtnClicked = true;
//                    hintBtn.setBackgroundColor(res.getColor(R.color.hint_btn_red));
                    hintBtn.setBackground(res.getDrawable(R.drawable.hint_btn_selected, getTheme()));
                    hintBtn.setTextColor(res.getColor(R.color.white));
                    Toast.makeText(getApplicationContext(), "Double Click On A Tile", Toast.LENGTH_SHORT).show();

                    // show hint
                }
                else {
                    setHintBtnToDefault();
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

    private void setHintBtnToDefault() {
        hintBtnClicked = false;

        Button hintBtn = (Button) findViewById(R.id.hint_btn);
        hintBtn.setBackground(res.getDrawable(R.drawable.btn_generic, getTheme()));
        hintBtn.setTextColor(res.getColor(R.color.black));
    }
}
