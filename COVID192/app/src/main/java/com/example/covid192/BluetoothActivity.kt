package com.example.covid192

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.BatchingListUpdateCallback



class BluetoothActivity : AppCompatActivity() {

    var m_bluetoothAdapter: BluetoothAdapter? = null
    lateinit var m_pairedDevices: Set<BluetoothDevice>
    val REQUEST_ENABLE_BLUETOOTH = 1
    val verify = 1
    val nextBluetooth: Button = findViewById(R.id.next_bluetooth)
    val backBluetooth: ImageView = findViewById(R.id.back_bluetooth)
    val infoPopUp: ImageView = findViewById(R.id.info_bluetooth)
    val dispositivos: ListView = findViewById(R.id.dispositivos)

    companion object{
        val EXTRA_ADDRESS: String = "Device_address"

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)
        supportActionBar?.hide()




        m_bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if(m_bluetoothAdapter==null){
            //O dispositivo não suporta bluetooth
            return
        }
        if(!m_bluetoothAdapter!!.isEnabled()){
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            startActivityForResult( enableBluetoothIntent, REQUEST_ENABLE_BLUETOOTH)
            nextBluetooth.setOnClickListener(pairedDeviceList())

        }




        backBluetooth.setOnClickListener {
            val intent = Intent(this@BluetoothActivity, AlertActivity::class.java)
            startActivity(intent)
        }

        infoPopUp.setOnClickListener {
            val window = PopupWindow(this)
            val view = layoutInflater.inflate(R.layout.activity_info_bluetooth, null)
            window.contentView = view
            Handler().postDelayed({
                window.dismiss()
            }, 5000)
            window.showAsDropDown(infoPopUp)
        }

    }
    private fun pairedDeviceList(){
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        m_pairedDevices = m_bluetoothAdapter!!.bondedDevices
        val list: ArrayList<BluetoothDevice> = ArrayList()
        if(!m_pairedDevices.isEmpty()){
            for (device: BluetoothDevice in m_pairedDevices){
                list.add(device)
                Log.i("device", ""+device)
            }
        } else {
           //Sem dispositivos pareados
        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
        dispositivos.adapter = adapter
        dispositivos.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val device: BluetoothDevice = list(position)
            val address: String = device.address
            val intent = Intent(this, ControlActivity::class.java)
            intent.putExtra(EXTRA_ADDRESS, address)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_ENABLE_BLUETOOTH){
            if(resultCode == Activity.RESULT_OK){
                if(m_bluetoothAdapter!!.isEnabled){
                    //Bluetooth habilitado
                }
                else {
                    //Bluetooth desabilitado
                }
            } else if (resultCode == Activity.RESULT_CANCELED){
                //Erro ao ligar o bluetooth
            }

        }
    }
}

private fun Button.setOnClickListener(pairedDeviceList: Unit) {

}
