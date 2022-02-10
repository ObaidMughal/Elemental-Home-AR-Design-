package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.Plane;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Camera;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.Sun;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.TransformableNode;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.jaredrummler.android.colorpicker.ColorPickerDialog;
import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import at.markushi.ui.CircleButton;

//Main Class our project
public class MainActivity extends AppCompatActivity implements ItemTabListenerRec, ColorPickerDialogListener{
    //Variable for our project
    private static final String TAG ="MainActivity";
    private static final int DIALOG_ID =0;
    ArFragment arFragment;
    RecyclerView recyclerView;
    ModelRenderable renderable;
    Anchor anchor;
    AnchorNode anchorNode;
    CircleButton deleteBtn;
    CircleButton resetBtn;
    CircleButton saveBtn;
    SwitchCompat deleteSwitch;
    ChipNavigationBar chipnav;
    ImageView selectedColor;
    private int fCount;
    boolean deleteOnTap = false;
    private LinearLayout bottomsheetlayout;
    BottomSheetBehavior sheetBehavior;
    String[] objectName;
    int[] objectImg;
    private File videoDirectory;

//the first method when the app is start(ANDROID LIFECYCLE)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//CONNECTION OF  WIDGETS WITH XML.....
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragmented);
        recyclerView = findViewById(R.id.recycleView);
        deleteSwitch = findViewById(R.id.deleteswitch);
        bottomsheetlayout = findViewById(R.id.bottomsheetlayout);
        chipnav = findViewById(R.id.chipnav);
        deleteBtn = findViewById(R.id.deleteBtn);
        resetBtn = findViewById(R.id.resetBtn);
        saveBtn = findViewById(R.id.saveBtn);
        selectedColor=findViewById(R.id.colorChange);

   //RECYCLE VIEW.....
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        objectName = new String[]{"Sofa", "Bed", "Table", "Dresser","Side Lamp"};
        objectImg = new int[]{R.drawable.sofa, R.drawable.bed, R.drawable.table, R.drawable.dresser,R.drawable.sidelamp};
        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));

       // CALLING METHOD FOR DETECTING PLANE ...
        arFragment.getArSceneView().getScene().addOnUpdateListener(this::onUpdate);

        //DELETE ON TAB BUTTON......
        deleteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                deleteOnTap = isChecked;
            }
        });


        //NAVIGATION CHIP FOR BOTTOM SHEET(4 CASES).....
        chipnav.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {

            //WE HAVE  FOUR CATEGORIES SO WE HAVE MAKE FOUR CASES......
            @Override
            public void onItemSelected(int id) {
                switch (id) {
                    case R.id.bedRoom:

                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        objectName = new String[]{"Sofa", "Bed", "Table", "Dresser","Side Lamp"};
                        objectImg = new int[]{R.drawable.sofa, R.drawable.bed, R.drawable.table, R.drawable.dresser,R.drawable.sidelamp};
                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));
                        break;

                    case R.id.lounge:


                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        objectName = new String[]{"Single Sofa", "Two Seater", "Combed Sofa", "Corner Sofa","Dinning Table","Dinning Set","Lamp"};
                        objectImg = new int[]{R.drawable.singlesofa, R.drawable.twoseater, R.drawable.sofacombed, R.drawable.cornersofa,R.drawable.dinningtable,R.drawable.dinningset,R.drawable.lamp};
                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));
                        break;

                    case R.id.kitchen:

                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        objectName = new String[]{"Fridge", "Modern Fridge", "Cooking Range", "Cabinet","Dish Washer","Dish Cabinet"};
                        objectImg = new int[]{R.drawable.fridge, R.drawable.modernfridge, R.drawable.cookingrange, R.drawable.cabinet,R.drawable.dishwasher,R.drawable.dishcabinet};
                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));
                        break;

                    case R.id.bathroom:

                        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.HORIZONTAL, false));
                        objectName = new String[]{"Sink", "Rounded Sink", "Toilet", "Modern Toilet","Bathtub","Dust Bin"};
                        objectImg = new int[]{R.drawable.sink, R.drawable.roundsink, R.drawable.toilet, R.drawable.moderntoilet,R.drawable.bathtub,R.drawable.dustbin};
                        recyclerView.setAdapter(new RecycleAdapter(MainActivity.this, objectName, objectImg, MainActivity.this));
                        break;




                }
            }
        });

        //BY DEFAULT BEDROOM CHIP NAVIGATION IS SELECTED AND HIGHLIGHTED.....
        chipnav.setItemSelected(R.id.bedRoom,true);

        //THIS  METHOD PROVIDE US NODE FOR TAP ONN DELETE FUNCTION...
        arFragment.getArSceneView().getScene()
                .addOnPeekTouchListener(new Scene.OnPeekTouchListener() {


                    @Override
                    public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                        arFragment.onPeekTouch(hitTestResult, motionEvent);

                        // Check for touching a Sceneform node
                        if (hitTestResult.getNode() != null) {

                            Node node = hitTestResult.getNode();

                            //CONDITION WHEN DELETE ON TAP SWITCH IS ON
                            if (deleteOnTap) {
                                deleteItemOnTab(node);
                            }


                        }
                    }
                });



        //ONCLICK METHOD FOR DELETE BUTTON......
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeAnchorNode(anchorNode);

            }
        });

        //ONCLICK METHOD FOR RESET BUTTON......
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetScene();

            }
        });

        //ONCLICK METHOD FOR SAVE BUTTON
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();

            }

        });

        //ONCLICK METHOD FOR CHANGE COLOR........
        selectedColor.setOnClickListener(v -> changeColor());


        //CODE FOR BOTTOM SHEET.....
        sheetBehavior = BottomSheetBehavior.from(bottomsheetlayout);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {

            //BY DEFAULT METHOD  OF BOTTOM SHEET
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });



    }

    //METHOD FOR FINDING ANCHOR IN A CENTER OF PLANE....
    private void onUpdate(FrameTime frameTime) {

        Frame frame = arFragment.getArSceneView().getArFrame();
        Collection<Plane> planes = frame.getUpdatedTrackables(Plane.class);

        for (Plane plane : planes) {

            if (plane.getTrackingState() == TrackingState.TRACKING) {

                System.out.println("checking" + plane.getTrackingState());
                anchor = plane.createAnchor(plane.getCenterPose());

                break;
            }


        }
    }

    //THIS METHOD WILL MATCH THE NAME WITH RECYCLE VIEW STRING LIST.....
    @Override
    public void getitemNameonTabListener(String name) {
        if (name.equals("Sofa")) {

            placeSofa(anchor);
        }
        if (name.equals("Bed")) {
            placeBed(anchor);
        }
        if (name.equals("Table")) {
            placeTable(anchor);
        }
        if (name.equals("Dresser")) {
            placeDressing(anchor);
        }
        if (name.equals("Side Lamp")){
            placeSideLamp(anchor);
        }
        if (name.equals("Single Sofa")){
            placeSingleSofa(anchor);
        }
        if (name.equals("Two Seater")){
            placeTwoSeater(anchor);
        }
        if (name.equals("Combed Sofa")){
            placeCombedSofa(anchor);
        }
        if (name.equals("Corner Sofa")){
            placeCornerSofa(anchor);
        }
        if (name.equals("Dinning Table")){
            placeDinningTable(anchor);
        }
        if (name.equals("Dinning Set")){
            placeDinningSet(anchor);
        }
        if (name.equals("Lamp")){
            placeLamp(anchor);
        }
        if (name.equals("Fridge")){
            placeFridge(anchor);
        }
        if (name.equals("Modern Fridge")){
            placeModernFridge(anchor);
        }
        if (name.equals("Cooking Range")){
            placeCookingRange(anchor);
        }
        if (name.equals("Cabinet")){
            placeCabinet(anchor);
        }
        if (name.equals("Dish Washer")){
            placeDishWasher(anchor);
        }
        if (name.equals("Dish Cabinet")){
            placeDishCabinet(anchor);
        }
        if (name.equals("Sink")){
            placeSink(anchor);
        }
        if (name.equals("Rounded Sink")){
            placeRoundedSink(anchor);
        }
        if (name.equals("Toilet")){
            placeToilet(anchor);
        }
        if (name.equals("Modern Toilet")){
            placeModernToilet(anchor);
        }
        if (name.equals("Bathtub")){
            placeBathtub(anchor);
        }
        if (name.equals("Dust Bin")){
            placeDustBin(anchor);
        }




        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
    }

    //METHOD FOR PLACING A MODEL.......
    public void placeSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.scene)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor, renderable);
                });


    }


    public void placeBed(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.bed)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor, renderable);
                });


    }

    public void placeTable(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.table)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor, renderable);
                });


    }

    public void placeDressing(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dressing)
                .build()
                .thenAccept(modelRenderable -> {
                  renderable = modelRenderable;

                    placeModel(anchor, renderable);
                });


    }
    public void placeSideLamp(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.sidelamp)
                .build()
                .thenAccept(modelRenderable -> {
                   renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeSingleSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.singlesofa)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeTwoSeater(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.twoseater)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCombedSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.combedsofa)
                .build()
                .thenAccept(modelRenderable -> {
                 renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCornerSofa(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.cornersofa)
                .build()
                .thenAccept(modelRenderable -> {
                   renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDinningTable(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dinningtable)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDinningSet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dinningset)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeLamp(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.lamp)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeFridge(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.fridge)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeModernFridge(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.modernfridge)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCookingRange(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.cookingrange)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeCabinet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.cabinet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDishCabinet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dishcabinet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeDishWasher(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dishwasher)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeSink(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.sink)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeRoundedSink(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.roundedsink)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeToilet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.ctoilet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeModernToilet(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.moderntoilet)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;

                    placeModel(anchor,  renderable);
                });


    }
    public void placeBathtub(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.bathtub)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;
                    placeModel(anchor,  renderable);

                });


    }
    public void placeDustBin(Anchor anchor) {

        ModelRenderable
                .builder()
                .setSource(this, R.raw.dustbin1)
                .build()
                .thenAccept(modelRenderable -> {
                    renderable = modelRenderable;
                    placeModel(anchor,  renderable);

                });


    }

    //METHOD FOR ADDING MODEL TO A SCENE(display ON SCREEN)......
    private void placeModel(Anchor anchor, ModelRenderable renderable) {
        anchorNode = new AnchorNode(anchor);

        //FOR SCALING AND ROTATION.........
        TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
        transformableNode.setParent(anchorNode);

        transformableNode.setRenderable(renderable);
        arFragment.getArSceneView().getScene().addChild(anchorNode);
        transformableNode.select();

    }


    //METHOD FOR DELETE A LAST PLACED MODEL......
    public void removeAnchorNode(AnchorNode nodeToremove) {

        if (nodeToremove != null) {
            arFragment.getArSceneView().getScene().removeChild(nodeToremove);
            nodeToremove.getAnchor().detach();
            nodeToremove.setParent(null);
            nodeToremove = null;


        }

    }

    //METHOD FOR RESET SCREEN SCENE BUTTON.......
    public void resetScene() {

        List<Node> children = new ArrayList<>(arFragment.getArSceneView().getScene().getChildren());
        for (Node node : children) {
            if (node instanceof AnchorNode) {
                if (((AnchorNode) node).getAnchor() != null) {
                    ((AnchorNode) node).getAnchor().detach();
                }
            }
            if (!(node instanceof Camera) && !(node instanceof Sun)) {
                node.setParent(null);
            }
        }


    }

    //THIS METHOD IS USED FOR PLACING
    public void addObjectToScene(Anchor anchor, ModelRenderable modelRenderablein, int resource) {

        ModelRenderable
                .builder()
                .setSource(this, resource)
                .build()
                .thenAccept(modelRenderable -> {
                    //  modelRenderablein = modelRenderable;

                    placeModel(anchor, modelRenderable);
                });


    }

    //THIS METHOD WILL DELETE THE SELECTED ITEM.......
    public void deleteItemOnTab(Node node) {
        if (node instanceof AnchorNode) {
            if (((AnchorNode) node).getAnchor() != null) {
                ((AnchorNode) node).getAnchor().detach();
            }
        }
        if (!(node instanceof Camera) && !(node instanceof Sun)) {
            node.setParent(null);
        }

    }

    //this code is for storage permission(if this code is missing the app will not grant storage permission
    @Override
    protected void onResume() {

        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions
                    (this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);


    }

    //METHOD FOR CHANGING COLOR.................
    private void changeColor() {

    ColorPickerDialog.newBuilder()
            .setDialogType(ColorPickerDialog.TYPE_CUSTOM)
            .setAllowPresets(false)
            .setDialogId(DIALOG_ID)
            .setColor(android.graphics.Color.TRANSPARENT)
            .setShowAlphaSlider(true)
            .show(this);


}

    //THIS METHOD WILL APPLY THE SELECTED COLOR TO 3D OBJECT.......
    @Override public void onColorSelected(int dialogId, int color) {
        Log.d(TAG, "onColorSelected() called with: dialogId = [" + dialogId + "], color = [" + color + "]");
        switch (dialogId) {
            case DIALOG_ID:


                BaseTransformableNode node = arFragment.getTransformationSystem().getSelectedNode();
                if (node != null) {


                    node.getRenderable().getMaterial().setFloat3("baseColorTint", new Color(color));
                    node.getRenderable().getMaterial().setFloat3("baseColorFactor", new Color(color));
                }
                Toast.makeText(MainActivity.this, "Selected Color: #" + Integer.toHexString(color), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    //FOR VALIDATION
    @Override public void onDialogDismissed(int dialogId) {
        Log.d(TAG, "onDialogDismissed() called with: dialogId = [" + dialogId + "]");
    }


    //THIS METHOD IS USED FOR SAVE THE DESIGN..................
    private void takePhoto() {
        ArSceneView view = arFragment.getArSceneView();

        // Create a bitmap the size of the scene view.
        final Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
                Bitmap.Config.ARGB_8888);

        // Create a handler thread to offload the processing of the image.
        final HandlerThread handlerThread = new HandlerThread("PixelCopier");
        handlerThread.start();
        // Make the request to copy.
        PixelCopy.request(view, bitmap, (copyResult) -> {
            if (copyResult == PixelCopy.SUCCESS) {
                try {
                    saveBitmapToDisk(bitmap,"screenshot"+(fCount++)+".jpg");
                } catch (IOException e) {
//                    Toast toast = Toast.makeText(this, "error"+e.toString(),
//                            Toast.LENGTH_LONG);
//                    toast.show();
                    return;
                }
                Toast.makeText(this, "Screenshot saved in /Pictures/Screenshots", Toast.LENGTH_SHORT).show();





            } else {

                Toast.makeText(this, "Failed to take screenshot", Toast.LENGTH_SHORT).show();

            }
            handlerThread.quitSafely();
        }, new Handler(handlerThread.getLooper()));
    }


    //THIS METHOD IS FOR SAVE A PICTURE IN LOCAL STORAGE..........
    public void saveBitmapToDisk(Bitmap bitmap,String filename) throws IOException {

        String path = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(path+File.separator+"/saved_images");
        myDir.mkdirs();

        String fname = filename;
        File file = new File (myDir, fname);

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Success to take screenshot", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Failed to take screenshot", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH.mm.ss");
        String formattedDate = df.format(c.getTime());

        File mediaFile = new File(videoDirectory, "FieldVisualizer"+formattedDate+".jpeg");

        FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, fileOutputStream);
        fileOutputStream.flush();
        fileOutputStream.close();
    }
}
