
fun consultarPedidos() {
    println("CONSULTA DE PEDIDOS")
    println("1. Todos")
    println("2. Por status")
    println("3. Voltar")

    when (readln()) {
        "1" -> mostrarPedidos(listaPedidos)
        "2" -> {
            println("Escolha o status:")
            StatusPedido.values().forEachIndexed { i, status ->
                println("${i + 1}. $status")
            }
            val escolha = readln().toIntOrNull()
            if (escolha != null && escolha in 1..StatusPedido.values().size) {
                val statusEscolhido = StatusPedido.values()[escolha - 1]
                mostrarPedidos(listaPedidos.filter { it.status == statusEscolhido })
            } else {
                println("Opção inválida!")
            }
        }
        "3" -> return
        else -> println("Opção inválida!")
    }
}

fun mostrarPedidos(pedidos: List<Pedido>) {
    if (pedidos.isEmpty()) {
        println("Nenhum pedido encontrado.")
    } else {
        pedidos.forEach {
            println("Pedido ${it.id} - Status: ${it.status}")
            it.itens.forEach { item ->
                println(" - ${item.nome} - R$ ${"%.2f".format(item.preco)}")
            }
        }
    }
}
