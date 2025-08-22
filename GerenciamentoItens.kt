
data class Item(var nome: String, var preco: Double)

val listaItens = mutableListOf<Item>()

fun gerenciarItens() {
    while (true) {
        println("GERENCIAMENTO DE ITENS")
        println("1. Adicionar item")
        println("2. Editar item")
        println("3. Remover item")
        println("4. Listar itens")
        println("5. Voltar ao menu principal")

        when (readln()) {
            "1" -> adicionarItem()
            "2" -> editarItem()
            "3" -> removerItem()
            "4" -> listarItens()
            "5" -> break
            else -> println("Opção inválida!")
        }
    }
}

fun adicionarItem() {
    print("Nome do item: ")
    val nome = readln()
    print("Preço (use vírgula ou ponto): ")
    val preco = readln().replace(",", ".").toDoubleOrNull()

    if (nome.isNotBlank() && preco != null) {
        listaItens.add(Item(nome, preco))
        println("Item adicionado!")
    } else {
        println("Dados inválidos!")
    }
}

fun editarItem() {
    listarItens()
    if (listaItens.isEmpty()) return

    print("Informe o número do item para editar: ")
    val index = readln().toIntOrNull()?.minus(1)

    if (index != null && index in listaItens.indices) {
        val item = listaItens[index]
        print("Novo nome (${item.nome}): ")
        val novoNome = readln()
        if (novoNome.isNotBlank()) item.nome = novoNome

        print("Novo preço (${item.preco}): ")
        val novoPreco = readln().replace(",", ".").toDoubleOrNull()
        if (novoPreco != null) item.preco = novoPreco

        println("Item atualizado!")
    } else {
        println("Item inválido!")
    }
}

fun removerItem() {
    listarItens()
    if (listaItens.isEmpty()) return

    print("Informe o número do item para remover: ")
    val index = readln().toIntOrNull()?.minus(1)

    if (index != null && index in listaItens.indices) {
        listaItens.removeAt(index)
        println("Item removido!")
    } else {
        println("Item inválido!")
    }
}

fun listarItens() {
    println("LISTA DE ITENS")
    if (listaItens.isEmpty()) {
        println("Nenhum item cadastrado.")
    } else {
        listaItens.forEachIndexed { i, item ->
            println("${i + 1}. ${item.nome} - R$ ${"%.2f".format(item.preco)}")
        }
    }
}
