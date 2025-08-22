
enum class StatusPedido {
    ACEITO, FAZENDO, FEITO, ESPERANDO_ENTREGADOR, SAIU_PARA_ENTREGA, ENTREGUE
}

data class Pedido(
    val id: Int,
    val itens: MutableList<Item>,
    var status: StatusPedido = StatusPedido.ACEITO,
    var desconto: Double = 0.0
)

val listaPedidos = mutableListOf<Pedido>()
var idPedidoAtual = 1

fun criarPedido() {
    if (listaItens.isEmpty()) {
        println("Não é possível criar um pedido sem itens cadastrados.")
        return
    }

    val itensPedido = mutableListOf<Item>()
    while (true) {
        listarItens()
        print("Escolha o número do item para adicionar (0 para finalizar): ")
        val escolha = readln().toIntOrNull()

        if (escolha == 0) break
        if (escolha != null && escolha in 1..listaItens.size) {
            itensPedido.add(listaItens[escolha - 1])
        } else {
            println("Escolha inválida.")
        }
    }

    if (itensPedido.isEmpty()) {
        println("O pedido deve ter pelo menos um item!")
        return
    }

    // Cupom de desconto
    print("Digite o cupom de desconto (ou pressione Enter para nenhum): ")
    val cupom = readln()
    val desconto = if (cupom.equals("DESCONTO10", ignoreCase = true)) 0.10 else 0.0

    val pedido = Pedido(idPedidoAtual++, itensPedido, desconto = desconto)
    listaPedidos.add(pedido)

    println("Pedido criado e pago automaticamente! Status: ${pedido.status}")
}
