package cl.rodofla.android.restorantapp

class CuentaMesa(val mesa : Int) {
    val _items : MutableList<ItemMesa> = mutableListOf<ItemMesa>()
    var aceptaPropina : Boolean = true
    fun agregarItem(itemMenu : ItemMenu, cantidad : Int) {
        val itemMesa = ItemMesa(itemMenu, cantidad)
        _items.add(itemMesa)
    }
    fun agregarItem(itemMenu : ItemMenu) {
        val itemMesa = ItemMesa(itemMenu, 1)
        _items.add(itemMesa)
    }
    fun calcularTotalSinPropina():Int {
        var total : Int = 0
        for(item in _items){
            total += item.calcularSubtotal()
        }
        return total
    }
    fun calcularPropina():Int {
        val total = calcularTotalSinPropina() * 0.1
        return total.toInt()
    }
    fun calcularTotalConPropina():Int {
        return calcularTotalSinPropina() + calcularPropina()
    }
}