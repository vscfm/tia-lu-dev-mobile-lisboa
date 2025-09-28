fun main() {
    var opcao: String

    do {
        println("\n===== LU DELIVERY =====")
        println("1. Cadastrar Item")
        println("2. Atualizar Item")
        println("3. Criar Pedido")
        println("4. Atualizar Pedido")
        println("5. Consultar Pedidos")
        println("0. Sair")
        print("Escolha uma opção: ")
        opcao = readln()

        when (opcao) {
            "1" -> {
                print("Nome: ")
                val nome = readln().uppercase()
                print("Descrição: ")
                val desc = readln()
                print("Preço: ")
                val preco = readln().replace(',', '.').toDoubleOrNull() ?: 0.0
                print("Quantidade: ")
                val qtd = readln().toIntOrNull() ?: 0

                val item = cadastrarItem(nome, desc, preco, qtd)
                println("Item cadastrado: $item")
            }

            "2" -> {
                println(listarItens())
                print("Código do item a atualizar: ")
                val cod = readln().toIntOrNull()
                if (cod != null) {
                    print("Novo nome (enter para manter): ")
                    val novoNome = readln().ifBlank { null }
                    print("Nova descrição (enter para manter): ")
                    val novaDesc = readln().ifBlank { null }
                    print("Novo preço (enter para manter): ")
                    val novoPreco = readln().toDoubleOrNull()
                    print("Nova quantidade (enter para manter): ")
                    val novaQtd = readln().toIntOrNull()

                    val resultado = if (atualizarItem(cod, novoNome, novaDesc, novoPreco, novaQtd))
                        "Item atualizado!"
                    else "Erro: item não encontrado."
                    println(resultado)
                }
            }

            "3" -> {
                println(listarItens())
                print("Código do item: ")
                val cod = readln().toIntOrNull()
                print("Quantidade: ")
                val qtd = readln().toIntOrNull() ?: 0
                print("Possui cupom? (S/N): ")
                val cupom = readln().equals("S", true)

                val resultado = criarPedido(cod ?: -1, qtd, cupom)
                println(resultado)
            }

            "4" -> {
                if (pedidos.isEmpty()) println("Nenhum pedido registrado.")
                else {
                    pedidos.forEach { println("Pedido ${it.codigo}: ${it.item.nome} (${it.quantidade} unid) - ${it.status}") }
                    print("Código do pedido a atualizar: ")
                    val cod = readln().toIntOrNull()
                    if (cod != null) {
                        println("Escolha novo status:")
                        StatusPedido.values().forEachIndexed { i, s -> println("${i + 1} - $s") }
                        val escolha = readln().toIntOrNull()
                        val status = escolha?.let { StatusPedido.values().getOrNull(it - 1) }
                        val resultado = if (status != null) atualizarPedido(cod, status) else "Erro: status inválido."
                        println(resultado)
                    }
                }
            }

            "5" -> {
                println("Consultar pedidos:")
                println("1 - Todos")
                StatusPedido.values().forEachIndexed { i, s -> println("${i + 2} - $s") }
                val escolha = readln().toIntOrNull()
                val filtro = when (escolha) {
                    1 -> null
                    in 2..(StatusPedido.values().size + 1) -> StatusPedido.values()[escolha!! - 2]
                    else -> null
                }
                val lista = consultarPedidos(filtro)
                if (lista.isEmpty()) println("Nenhum pedido encontrado.")
                else lista.forEach {
                    println("Pedido ${it.codigo}: ${it.item.nome} (${it.quantidade} unid) - ${it.status}")
                }
            }

            "0" -> println("Encerrando sistema...")
            else -> println("Opção inválida!")
        }
    } while (opcao != "0")
}
