package com.example.minesweeper;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    /*
    * 블럭 배열
    * */
    BlockButton[][] buttons = new BlockButton[9][9];
    //토글 버튼 만들기
    ToggleButton toggleButton;
    //마인 개수와 승리/패배 여부 출력할 textView
    TextView textView;
    //게임이 종료되었음을 판단하는 boolean
    boolean end=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(); //게임판 초기화 메소드

        //텍스트뷰 설정
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Mines : 10");
        textView.setTextSize(25);

        //토글버튼 설정 (Break Mode / Toggle Mode)
        toggleButton = (ToggleButton) findViewById(R.id.toggleButton);
        toggleButton.setText("Break");
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleButton.isChecked()) {
                    toggleButton.setText("Toggle");
                        for (int i = 0; i < 9; i++) {
                            for (int j = 0; j < 9; j++) {
                                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        ((BlockButton) v).toggleFlag();
                                    }
                                });
                                if (!(buttons[i][j].getText().equals("")) && !(buttons[i][j].getText().equals("+"))) {
                                    buttons[i][j].setClickable(false);
                                }
                            }
                        }
                } else {
                    toggleButton.setText("Break");
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 9; j++) {
                            buttons[i][j].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    BlockButton b = (BlockButton) v;
                                    end = b.breakBlock(b.x, b.y);
                                    if(end)
                                    {
                                        gameEnd();
                                    }

                                }
                            });
                            if (buttons[i][j].flag) {
                                buttons[i][j].setClickable(false);
                            }
                            if (!(buttons[i][j].getText().equals("")) && !(buttons[i][j].getText().equals("+"))) {
                                buttons[i][j].setClickable(false);
                            }
                        }
                    }

                }
            }
        });
    }

    /*
     * void 메소드
     *
     * 하는 일 : 테이블 레이아웃 내의 TableRow작성 및 버튼 삽입
     *
     * TableRow생성 -> 안에 버튼 9개 넣기 -> 그걸 TableLayout에 삽입
     * 9번 반복
     * 지뢰 매설
     */
    protected void init() {
        buttons = new BlockButton[9][9];
        TableLayout table = (TableLayout) findViewById(R.id.tableLayout);
        // 레이아웃 설정
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);

            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new BlockButton(this, i, j);
                buttons[i][j].setLayoutParams(layoutParams);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        BlockButton b = (BlockButton) v;
                        end = b.breakBlock(b.x, b.y);
                        if(end)
                        {
                            //게임 종료시 게임 종료하는 메소드 호출
                            gameEnd();
                        }
                    }
                });
                tableRow.addView(buttons[i][j]);
            }
            table.addView(tableRow);
        }
        int totalMine = 0;
        Random random = new Random();

        while (totalMine != 10) {
            int randomX = random.nextInt(9);
            int randomY = random.nextInt(9);
            if (!buttons[randomX][randomY].mine) {
                buttons[randomX][randomY].mine = true;
                totalMine++;

            }

        }

        for (int q = 0; q < 9; q++) {
            if (q != 0) {
                //바로 왼쪽
                if (buttons[0][q - 1].mine) {
                    buttons[0][q].neighborMines++;
                }
                //왼쪽 아래
                if (buttons[1][q - 1].mine) {
                    buttons[0][q].neighborMines++;
                }
            }
            if (q != 8) {
                //오른쪽 아래
                if (buttons[1][q + 1].mine) {
                    buttons[0][q].neighborMines++;
                }
                //오른쪽
                if (buttons[0][q + 1].mine) {
                    buttons[0][q].neighborMines++;
                }
            }
            //아래
            if (buttons[1][q].mine) {
                buttons[0][q].neighborMines++;
            }
        }
        for (int p = 1; p < 8; p++) {
            //위
            if (buttons[p - 1][0].mine) {
                buttons[p][0].neighborMines++;
            }
            //오른쪽 아래
            if (buttons[p + 1][1].mine) {
                buttons[p][0].neighborMines++;
            }
            //오른쪽
            if (buttons[p][1].mine) {
                buttons[p][0].neighborMines++;
            }
            //오른쪽 위
            if (buttons[p - 1][1].mine) {
                buttons[p][0].neighborMines++;
            }
            //아래
            if (buttons[p + 1][0].mine) {
                buttons[p][0].neighborMines++;
            }
        }

        for (int p = 1; p < 8; p++) {
            for (int q = 1; q < 8; q++) {
                //왼쪽 위
                if (buttons[p - 1][q - 1].mine) {
                    buttons[p][q].neighborMines++;
                }
                //바로 왼쪽
                if (buttons[p][q - 1].mine) {
                    buttons[p][q].neighborMines++;
                }
                //왼쪽 아래
                if (buttons[p + 1][q - 1].mine) {
                    buttons[p][q].neighborMines++;
                }
                //위
                if (buttons[p - 1][q].mine) {
                    buttons[p][q].neighborMines++;
                }
                //오른쪽 아래
                if (buttons[p + 1][q + 1].mine) {
                    buttons[p][q].neighborMines++;
                }
                //오른쪽
                if (buttons[p][q + 1].mine) {
                    buttons[p][q].neighborMines++;
                }
                //오른쪽 위
                if (buttons[p - 1][q + 1].mine) {
                    buttons[p][q].neighborMines++;
                }
                //아래
                if (buttons[p + 1][q].mine) {
                    buttons[p][q].neighborMines++;
                }
            }
        }

        for (int q = 0; q < 9; q++) {
            if (q != 0) {
                //바로 왼쪽
                if (buttons[8][q - 1].mine) {
                    buttons[8][q].neighborMines++;
                }
                //왼쪽 위
                if (buttons[7][q - 1].mine) {
                    buttons[8][q].neighborMines++;
                }

            }
            if (q != 8) {
                //오른쪽
                if (buttons[8][q + 1].mine) {
                    buttons[8][q].neighborMines++;
                }
                //오른쪽 위
                if (buttons[7][q + 1].mine) {
                    buttons[8][q].neighborMines++;
                }
            }
            //위
            if (buttons[7][q].mine) {
                buttons[8][q].neighborMines++;
            }
        }

        for (int p = 1; p < 8; p++) {
            //왼쪽 위
            if (buttons[p - 1][7].mine) {
                buttons[p][8].neighborMines++;
            }
            //바로 왼쪽
            if (buttons[p][7].mine) {
                buttons[p][8].neighborMines++;
            }
            //왼쪽 아래
            if (buttons[p + 1][7].mine) {
                buttons[p][8].neighborMines++;
            }
            //위
            if (buttons[p - 1][8].mine) {
                buttons[p][8].neighborMines++;
            }
            //아래
            if (buttons[p + 1][8].mine) {
                buttons[p][8].neighborMines++;
            }

        }


    }



    //꽂힌 플래그
    public static int flaggedFlag = 0;
    //남은 블럭
    public static int remainBlocks = 0;
    class BlockButton extends androidx.appcompat.widget.AppCompatButton {
        /**
         * flaggedFlags와
         * remainBlock은 상단에 있음
         */

        TableRow.LayoutParams layoutParams;
        public int x;
        public int y;
        public boolean mine = false;
        public boolean flag = false;
        public int neighborMines = 0;

        /*
         *
         * 블록 버튼 생성자
         *
         * */
        public BlockButton(Context context, int x, int y) {
            super(context);
            layoutParams = new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            this.setLayoutParams(layoutParams);
            this.x = x;
            this.y = y;
            remainBlocks++;
        }

        /*
         * 플래그 바꾸기
         */
        public void toggleFlag() {

            if (this.flag) {
                flag = false;
                this.setText("");
                flaggedFlag--;
                textView.setText("Mines : " + String.valueOf(10 - flaggedFlag));
            } else {
                if (flaggedFlag < 10) {
                    flag = true;
                    this.setText("+");
                    flaggedFlag++;
                    textView.setText("Mines : " + String.valueOf(10 - flaggedFlag));
                }
            }

        }

        /*
         * 블럭 오픈
         * x,y자신의 위치값
         */
        public boolean breakBlock(int x, int y) {
            //만약 자신이 말도 안되는 위치에 있을 경우
            if (x < 0 && x > 8 && y > 0 && y < 8)
                return false;
            BlockButton b = this;
            //클릭이 안되는 상황에서 클릭 되었을 경우
            if (b.isClickable() == false) {
                return false;
            }
            else if (b.mine) {
                //마인이면 return true반환 gameEnd() 실행됨
                b.setText("*");
                b.setClickable(false);
                return true;
            }
            else if (b.neighborMines == 0 ) {
                //본인 주변에 마인이 없을 경우 재귀 호출된다.
                b.setText(String.valueOf(b.neighborMines));
                b.setClickable(false);
                if (x - 1 > -1 && x - 1 < 9 && y - 1 > -1 && y - 1 < 9)
                    buttons[b.x - 1][b.y - 1].breakBlock(b.x - 1, b.y - 1);
                if (x > -1 && x < 9 && y - 1 > -1 && y - 1 < 9)
                    buttons[b.x][b.y - 1].breakBlock(b.x, b.y - 1); //좌
                if (x + 1 > -1 && x + 1 < 9 && y - 1 > -1 && y - 1 < 9)
                    buttons[b.x + 1][b.y - 1].breakBlock(b.x + 1, b.y - 1); //좌하
                if (x + 1 > -1 && x + 1 < 9 && y > -1 && y < 9)
                    buttons[b.x + 1][b.y].breakBlock(b.x + 1, b.y); //하
                if (x - 1 > -1 && x - 1 < 9 && y > -1 && y < 9)
                    buttons[b.x - 1][b.y].breakBlock(b.x - 1, b.y); //상
                if (x - 1 > -1 && x - 1 < 9 && y + 1 > -1 && y + 1 < 9)
                    buttons[b.x - 1][b.y + 1].breakBlock(b.x - 1, b.y + 1);//우상
                if (x > -1 && x < 9 && y + 1 > -1 && y + 1 < 9)
                    buttons[b.x][b.y + 1].breakBlock(b.x, b.y + 1); //우
                if (x + 1 > -1 && x + 1 < 9 && y + 1 > -1 && y + 1 < 9)
                    buttons[b.x + 1][b.y + 1].breakBlock(b.x + 1, b.y + 1);//우하
            }
            else {
                //그 외의 경우는 그냥 표시됨
                b.setText(String.valueOf(b.neighborMines));
                b.setClickable(false);
            }
            //메소드 불린 만큼 블록수 감소
            remainBlocks--;
            //만약 열었는데 남은 블록이 10개다.(즉 죽지 않고 블록 10개만 남겼다 == 지뢰를 모두 찾았다.)
            if(remainBlocks==10) {
                end=true;
                gameEnd();
            }
            return false;
        }


    }

    void gameEnd()
    {
        end=true;
        //명시적으로 게임이 끝났음을 알림
        if(remainBlocks==10) //남은 블록이 10개다 지뢰를 모두 찾았다.
        {
            textView.setText("YOU WIN!!!");
        }
        else //그 외 상황은 마인을 밟았을 때만 열리므로
        {
            textView.setText("GAME OVER!");
        }
        //모든 버튼을 누를 수 없는 상태로 만든다.
        for(int i=0;i<9;i++) {
            for (int j = 0; j < 9; j++)
            {
                buttons[i][j].setClickable(false);
            }
        }
        toggleButton.setClickable(false);
    }
}