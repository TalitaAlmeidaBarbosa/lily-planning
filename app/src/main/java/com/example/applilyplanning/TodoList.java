package com.example.applilyplanning;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.applilyplanning.database.RetrofitConfig;
import com.example.applilyplanning.model.Aluno;
import com.example.applilyplanning.model.Anotacao;
import com.example.applilyplanning.model.Professor;
import com.example.applilyplanning.model.ToDo;
import com.example.applilyplanning.model.Token;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodoList extends AppCompatActivity {

    private RecyclerView taskRecyclerView;
    private ToDoAdaptador taskAdapter;
    EditText edtNewTask;
    Button btnNewTask;
    CheckBox anotacao;

    List<ToDo> listaDeTarefas;
    AlertDialog.Builder builder;
    AlertDialog dialog;
    private List<ToDo> listaTarefas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_todo_list);
        listaDeTarefas = new ArrayList<ToDo>();

        LayoutInflater inflater = this.getLayoutInflater();
        View titleView = inflater.inflate(R.layout.alert_task, null);

//        ArrayList<String> tarefasAnteriores = new ArrayList<String>(); // Temporário
//        tarefasAnteriores.add("Fazer chicão");
//        tarefasAnteriores.add("Fazer API de práticas");
//        tarefasAnteriores.add("Estudar Português");

        AppCompatButton button = findViewById(R.id.btnAdicionar);

        builder = new AlertDialog.Builder(TodoList.this).setCustomTitle(titleView);
        builder.setCancelable(true);
        View v = LayoutInflater.from(TodoList.this).inflate(R.layout.new_task, null, false);
        builder.setView(v);

        dialog = builder.create();

        Intent intent = getIntent();
        Bundle params = intent.getExtras();

        Integer tokenRecebido = params.getInt("token");

        //String aluno = params.getString("email_aluno");
        // Aluno alunoRecebido = (Aluno) getIntent().getSerializableExtra("aluno");

        if (params != null) {
            //String tokenRecebido = params.getString("token");
            Service service = RetrofitConfig.getRetrofitInstance().create(Service.class);
            /*final Aluno[] alunoResponse = {null};
            final int[] idAluno = {0};*/
            /*Service service = RetrofitConfig.getRetrofitInstance().create(Service.class);

            Call<Aluno> call2 = service.selecionarAlunoId(Integer.parseInt(tokenRecebido));
            //final Integer[] idAluno = {0};

            final Aluno[] alunoLegal = new Aluno[1];

            call2.enqueue(new Callback<Aluno>() {
                @Override
                public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                    if (response.isSuccessful())
                    {
                        Aluno alunoResponse = response.body();

                        alunoLegal[0] = new Aluno(alunoResponse.getNome_aluno(), alunoResponse.getSenha_aluno(), alunoResponse.getEmail_aluno(), alunoResponse.getId_aluno());

                        //idAluno[0] = alunoResponse.getId_aluno();

                        //alunoResponse.getId_aluno();
//                        alunoResponse[0] = response.body();
//                        idAluno[0] = alunoResponse[0].getId_aluno();
                        Toast.makeText(TodoList.this, /*idAluno[0]*/ /*alunoLegal[0].getId_aluno().toString(), Toast.LENGTH_LONG).show();*/
                 /*   }
                }

                @Override
                public void onFailure(Call<Aluno> call, Throwable t) {
                    Toast.makeText(TodoList.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });*/

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.show();

                    btnNewTask = v.findViewById(R.id.btnNewTask);

                    btnNewTask.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            edtNewTask = v.findViewById(R.id.edtNewTask);

                            String anotation = edtNewTask.getText().toString();

                            Anotacao anotacao = new Anotacao(anotation, tokenRecebido);
                            Call<Anotacao> call = service.incluirAnotacao(anotacao);

                            call.enqueue(new Callback<Anotacao>() {
                                @Override
                                public void onResponse(Call<Anotacao> call, Response<Anotacao> response) {
                                    if (response.isSuccessful()){
                                        Toast.makeText(TodoList.this, "Anotação incluída!", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Anotacao> call, Throwable t) {
                                    Toast.makeText(TodoList.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                            //tarefasAnteriores.add(edtNewTask.getText().toString());
                            dialog.dismiss();

                            //listaDeTarefas.add(new ToDo(tarefasAnteriores.get(tarefasAnteriores.size() - 1)));

                            edtNewTask.setText("");
                        }

                    });
                }
            });

//           edtNewTask = v.findViewById(R.id.edtNewTask);
//           Anotacao anotacao = new Anotacao(edtNewTask.getText().toString(), Integer.parseInt(tokenRecebido));
/*           Call<Aluno> call = service.selecionarAluno(tokenRecebido);

            call.enqueue(new Callback<Aluno>() {
                @Override
                public void onResponse(Call<Aluno> call, Response<Aluno> response) {
                    if (response.isSuccessful()){
                        Aluno aln = response.body();
*/
                        Call<List<Anotacao>> callAnot = service.selecionarAnotacaoFk(tokenRecebido);
                        callAnot.enqueue(new Callback<List<Anotacao>>() {
                            @Override
                            public void onResponse(Call<List<Anotacao>> call, Response<List<Anotacao>> response) {
                                taskAdapter = new ToDoAdaptador(response.body());
                                taskRecyclerView = findViewById(R.id.recyclerView);
                                taskRecyclerView.setAdapter(taskAdapter);
                            }

                            @Override
                            public void onFailure(Call<List<Anotacao>> call, Throwable t) {
                                Toast.makeText(TodoList.this, "talda depressao", Toast.LENGTH_SHORT).show();
                            }
                        });
                   }
                    else
                        Toast.makeText(TodoList.this, "deu errado", Toast.LENGTH_SHORT).show();
/*                }

                @Override
                public void onFailure(Call<Aluno> call, Throwable t) {
                    Toast.makeText(TodoList.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }*/

        /*Call<List<Anotacao>> call = service.selecionarAnotacaoFk();

        call.enqueue(new Callback<List<Anotacao>>() {
            @Override
            public void onResponse(Call<List<Anotacao>> call, Response<List<Anotacao>> response) {
                taskAdapter = new ToDoAdaptador(response.body());
                taskRecyclerView = findViewById(R.id.recyclerView);
                taskRecyclerView.setAdapter(taskAdapter);
            }

            @Override
            public void onFailure(Call<List<Anotacao>> call, Throwable t) {
                Toast.makeText(TodoList.this, "talda depressao", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*for (String toDo : tarefasAnteriores) {
            listaDeTarefas.add(new ToDo(toDo));
        }*/
        /*taskAdapter = new ToDoAdaptador(listaDeTarefas);
        taskRecyclerView = findViewById(R.id.recyclerView);
        taskRecyclerView.setAdapter(taskAdapter);*/

        /*
        listaTarefas = new ArrayList<>();

        taskRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new ToDoAdaptador(listaTarefas);
        taskRecyclerView.setAdapter(taskAdapter);

        ToDoModel task = new ToDoModel();
        //task.setDataInicio("24/09/2022");
        //task.setDataEntrega("25/09/2022");
        task.setNomeLista("Teste lista");
        //task.setStatus(0);
        //task.setIdLista(1);

        listaTarefas.add(task);

        taskAdapter.setTasks(listaTarefas);
        taskAdapter.notifyDataSetChanged();

         */
    }

    public void IrHome(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}