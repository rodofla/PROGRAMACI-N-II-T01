package cl.rodofla.android.restorantapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {

    private var textViewPastel: TextView? = null
    private var textViewCazuela: TextView? = null
    private var textViewComida: TextView? = null
    private var textViewPropina: TextView? = null
    private var textViewTotal: TextView? = null
    private var editTextPastel: EditText? = null
    private var editTextCazuela: EditText? = null
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private var switchPropina: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        editTextPastel = findViewById(R.id.etPastel)
        editTextCazuela = findViewById(R.id.etCazuela)
        switchPropina = findViewById(R.id.swPropina)
        textViewPastel = findViewById(R.id.tvTotalPastel)
        textViewCazuela = findViewById(R.id.tvTotalCazuela)
        textViewComida = findViewById(R.id.tvComida)
        textViewPropina = findViewById(R.id.tvPropina)
        textViewTotal = findViewById(R.id.tvTotal)


        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                mostrarTotal()
            }
        }

        editTextPastel?.addTextChangedListener(textWatcher)
        editTextCazuela?.addTextChangedListener(textWatcher)

        switchPropina?.setOnCheckedChangeListener { _, _ ->
            mostrarTotal()
        }
    }

    private fun mostrarTotal() {
        val cuentaMesa = CuentaMesa(1)
        cuentaMesa.aceptaPropina = switchPropina?.isChecked == true

        val pastelMesa = ItemMesa(ItemMenu("Pastel de Choclo", "12000"), editTextPastel?.text.toString().toIntOrNull() ?: 0)
        val cazuelaMesa = ItemMesa(ItemMenu("Cazuela", "10000"), editTextCazuela?.text.toString().toIntOrNull() ?: 0)

        cuentaMesa.agregarItem(pastelMesa.itemMenu, pastelMesa.cantidad)
        cuentaMesa.agregarItem(cazuelaMesa.itemMenu, cazuelaMesa.cantidad)

        val totalPastel = "$" + NumberFormat.getInstance().format(pastelMesa.calcularSubtotal())
        val totalCazuela = "$" + NumberFormat.getInstance().format(cazuelaMesa.calcularSubtotal())
        val totalComida = "$" + NumberFormat.getInstance().format(cuentaMesa.calcularTotalSinPropina())

        textViewPastel?.text = totalPastel
        textViewCazuela?.text = totalCazuela
        textViewComida?.text = totalComida

        val propina: String
        val totalFinal: String
        if (cuentaMesa.aceptaPropina) {
            propina = "$" + NumberFormat.getInstance().format(cuentaMesa.calcularPropina())
            totalFinal = "$" + NumberFormat.getInstance().format(cuentaMesa.calcularTotalConPropina())
        } else {
            propina = "$0"
            totalFinal = totalComida
        }

        textViewPropina?.text = propina
        textViewTotal?.text = totalFinal
    }
}