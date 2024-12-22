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

        configurarVista()
        configurarEventos()
    }

    private fun configurarVista() {
        enableEdgeToEdge()
        aplicarMargenesDeBarraDeSistema(findViewById(R.id.main))

        editTextPastel = findViewById(R.id.etPastel)
        editTextCazuela = findViewById(R.id.etCazuela)
        switchPropina = findViewById(R.id.swPropina)
        textViewPastel = findViewById(R.id.tvTotalPastel)
        textViewCazuela = findViewById(R.id.tvTotalCazuela)
        textViewComida = findViewById(R.id.tvComida)
        textViewPropina = findViewById(R.id.tvPropina)
        textViewTotal = findViewById(R.id.tvTotal)
    }

    private fun aplicarMargenesDeBarraDeSistema(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configurarEventos() {
        val textWatcher = crearTextWatcher { mostrarTotal() }
        editTextPastel?.addTextChangedListener(textWatcher)
        editTextCazuela?.addTextChangedListener(textWatcher)

        switchPropina?.setOnCheckedChangeListener { _, _ ->
            mostrarTotal()
        }
    }

    private fun crearTextWatcher(onTextChanged: () -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                onTextChanged()
            }
        }
    }


    private fun mostrarTotal() {
        val cuentaMesa = configurarCuentaMesa()
        val itemsMesa = obtenerItemsMesa()

        itemsMesa.forEach { cuentaMesa.agregarItem(it.itemMenu, it.cantidad) }

        actualizarVistaConTotales(cuentaMesa, itemsMesa)
    }

    private fun configurarCuentaMesa(): CuentaMesa {
        return CuentaMesa(1).apply {
            aceptaPropina = switchPropina?.isChecked == true
        }
    }

    private fun obtenerItemsMesa(): List<ItemMesa> {
        return listOf(
            crearItemMesa("Pastel de Choclo", 12000, editTextPastel),
            crearItemMesa("Cazuela", 10000, editTextCazuela)
        )
    }

    private fun crearItemMesa(nombre: String, precio: Int, editTextCantidad: EditText?): ItemMesa {
        val cantidad = editTextCantidad?.text.toString().toIntOrNull() ?: 0
        return ItemMesa(ItemMenu(nombre, precio.toString()), cantidad)
    }

    private fun actualizarVistaConTotales(cuentaMesa: CuentaMesa, itemsMesa: List<ItemMesa>) {
        val formatter = NumberFormat.getInstance()

        itemsMesa.forEach { item ->
            val subtotal = "$" + formatter.format(item.calcularSubtotal())
            when (item.itemMenu.nombre) {
                "Pastel de Choclo" -> textViewPastel?.text = subtotal
                "Cazuela" -> textViewCazuela?.text = subtotal
            }
        }

        textViewComida?.text = "$" + formatter.format(cuentaMesa.calcularTotalSinPropina())

        if (cuentaMesa.aceptaPropina) {
            textViewPropina?.text = "$" + formatter.format(cuentaMesa.calcularPropina())
            textViewTotal?.text = "$" + formatter.format(cuentaMesa.calcularTotalConPropina())
        } else {
            textViewPropina?.text = "$0"
            textViewTotal?.text = textViewComida?.text
        }
    }

}