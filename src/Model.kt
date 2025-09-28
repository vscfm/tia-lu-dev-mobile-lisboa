// =============================
// Model.kt
// Lógica de negócio e estrutura de dados
// =============================

data class Item(
    val codigo: Int,
    var nome: String,
    var descricao: String,
    var preco: Double,
    var quantidade: Int
)

enum class StatusPedido {
    ACEITO, FAZENDO, FEITO, ESPERANDO_ENTREGADOR, SAIU_PARA_ENTREGA, ENTREGUE
}

data class Pedido(
    val codigo: Int,
    val item: Item,
    var quantidade: Int,
    var status: StatusPedido,
    var precoFinal: Double
)

// Listas globais
val itens = mutableListOf<Item>()
val pedidos = mutableListOf<Pedido>()

// Contadores de código
var codigoAtualItem = 1
var codigoAtualPedido = 1

// =============================
// Funções de gerenciamento de itens
// =============================

fun cadastrarItem(nome: String, descricao: String, preco: Double, quantidade: Int): Item {
    val item = Item(codigoAtualItem++, nome, descricao, preco, quantidade)
    itens.add(item)
    return item
}

fun atualizarItem(
    codigo: Int,
    novoNome: String?,
    novaDescricao: String?,
    novoPreco: Double?,
    novaQtd: Int?
): Boolean {
    val item = itens.find { it.codigo == codigo } ?: return false
    novoNome?.takeIf { it.isNotBlank() }?.let { item.nome = it }
    novaDescricao?.takeIf { it.isNotBlank() }?.let { item.descricao = it }
    novoPreco?.takeIf { it >= 0 }?.let { item.preco = it }
    novaQtd?.takeIf { it >= 0 }?.let { item.quantidade = it }
    return true
}

fun listarItens(): String {
    return if (itens.isEmpty()) "Nenhum item cadastrado."
    else itens.joinToString("\n") {
        "Código ${it.codigo} - ${it.nome}: ${it.descricao} (R$ ${"%.2f".format(it.preco)}, ${it.quantidade} unid)"
    }
}

// =============================
// Funções de gerenciamento de pedidos
// =============================

fun criarPedido(codItem: Int, qtd: Int, cupom: Boolean = false): String {
    val item = itens.find { it.codigo == codItem } ?: return "Erro: item não encontrado."
    if (qtd <= 0) return "Erro: quantidade inválida."
    if (qtd > item.quantidade) return "Erro: estoque insuficiente."

    val desconto = if (cupom) 0.1 else 0.0
    val precoFinal = item.preco * qtd * (1 - desconto)

    pedidos.add(Pedido(codigoAtualPedido++, item, qtd, StatusPedido.ACEITO, precoFinal))
    item.quantidade -= qtd
    return "Pedido criado! Valor final: R$ ${"%.2f".format(precoFinal)}"
}

fun atualizarPedido(codigo: Int, novoStatus: StatusPedido): String {
    val pedido = pedidos.find { it.codigo == codigo } ?: return "Erro: pedido não encontrado."
    pedido.status = novoStatus
    return "Status atualizado para $novoStatus!"
}

fun consultarPedidos(filtro: StatusPedido?): List<Pedido> {
    return if (filtro == null) pedidos else pedidos.filter { it.status == filtro }
}
