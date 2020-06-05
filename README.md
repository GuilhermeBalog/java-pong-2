# Pong 2.0

## Descrição

Após o sucesso de [Pong!](https://github.com/GuilhermeBalog/java-pong), lançado pela *EACH Game Dev. Co.* e finalizado graças a sua
ajuda, cabe a você a tarefa de implementar alguns recursos novos a serem incorporados na
nova versão do jogo (**Pong! 2.0**). Mais especificamente, sua tarefa consiste em
**implementar uma nova classe** e **completar uma classe já existente**.

A nova classe que deve ser implementada é a **FxBall**. Seu papel é fornecer uma nova
implementação de bola que acrescenta um efeito visual de *rastro* (veja o vídeo atualizado
do *gameplay* para entender melhor do que se trata). A nova classe deve, obrigatoriamente,
implementar a interface **IBall**. Além disso, você deve reaproveitar a implementação já
existente na classe **Ball**, seja através de herança, ou seja através de composição. Ao
reaproveitar o que já existe na classe **Ball**, evita-se a reimplementação dos
comportamentos básicos da bola como: movimentação, verificação de colisão, e alteração
do movimento da bola devido a colisões (a propósito, o forma como a bola rebate nos
players foi alterada para deixar o jogo menos previsível e mais dinâmico). Esta classe deve,
**obrigatoriamente**, possuir um construtor com assinatura idêntica à do construtor da classe
**Ball**.

Além de gerenciar a eventual existência de múltiplas instâncias de bola, você também
precisa completar o código da classe **BallManager** de modo que ela seja responsável por
gerenciar a interação da(s) bola(s) com um tipo novo de elemento presente no jogo: os
targets (alvos). Na versão 2.0 do jogo, existem dois tipos de alvo: **DuplicatorTarget** e
**BoostTarget**, ambos derivados da classe mãe **Target**. Os alvos são similares às paredes
(instâncias da classe **Wall**), mas ao invés de rebater a bola, provocam algum efeito quando
atingidos, com cada tipo de alvo causando um efeito diferente.

Quando um alvo do tipo **BoostTarget** é atingido por uma bola, a bola deve ter sua
velocidade multiplicada pelo fator definido na constante **BOOST_FACTOR** (declarada na
própria classe **BoostTarget**) e tal efeito deve durar pelo intervalo de tempo (em
milisegundos) definido na constante **BOOST_DURATION** (também declarada em
BoostTarget). Passado o intervalo de tempo, a velocidade deve ser restaurada para a
velocidade padrão. Se uma bola, já com a velocidade aumentada atingir novamente um
BoostTarget, nenhuma ação deve ser realizada.

Já quando um alvo do tipo **DuplicatorTarget** é atingido por uma bola, uma nova bola
deverá ser adicionada ao jogo. A nova bola deverá surgir na mesma posição da bola que
atingiu o alvo, mas com uma nova direção (que deve ser determinada pela soma de uma
perturbação aleatória à direção da bola que atingiu o alvo). A nova bola deverá possuir a
velocidade padrão, não importando se a bola que colidiu com o **DuplicatorTarget**, está (ou
não) sob o efeito de ter atingido um **BoostTarget** previamente. Além disso, a nova bola
deve ter cor diferente da bola principal (aquela que existe desde o início da partida), e deve
expirar após o intervalo de tempo (em milisegundos) definido pela constante
**EXTRA_BALL_DURATION** (na própria classe **DuplicatorTarget**). A nova bola, com
exceção da cor diferente e do fato de expirar após um intervalo de tempo, deve se
comportar exatamente como a bola principal do jogo em relação ao seu comportamento de
movimento e pontuação.

Note que as classes **BoostTarget** e **DuplicatorTarget** já se encontram prontas, e não
devem ser alteradas. Estas duas classes são, em essência, muito parecidas (a rigor,
apenas a superclasse **Target** seria necessária), mas o fato de existirem dois tipos distintos
poderá ser conveniente para determinar com qual tipo de alvo uma bola colidiu. Observe
ainda que toda interação entre as bolas e os alvos, bem como a aplicação e gerenciamento
dos efeitos, devem ficar sob responsabilidade da classe **BallManager**.

Em resumo, na sua versão 2.0, o projeto [Pong!](https://github.com/GuilhermeBalog/java-pong) é composto pelas seguintes classes:

- **Pong**: classe principal do jogo (contém o método main que gerencia o todo andamento da partida e a interação entre os demais elementos).
- **GameLib/MyFrame/MyKeyAdapter**: três classes que implementam funcionalidades gráficas (criação de janela em modo gráfico, métodos para desenhos de formas geométricas) e para processar entrada via teclado.
- **IBall**: declara a interface comum a todas as bolas que venham a ser implementadas.
- **Ball**: implementa a bola “padrão” do jogo.
- **Player**: implementa os jogadores (controláveis) pelo usuários.
- **Wall**: implementa as paredes do jogo.
- **Score**: implementa o placar do jogo.
- **Target/BoostTarget/DuplicatorTarget**: implementação dos alvos que produzem algum tipo de efeito no jogo, quando atingidos por uma bola.
- **BallManager**: gerencia uma ou mais bolas presentes no jogo, assim como a interação das mesmas com os alvos e a aplicação dos efeitos correspondentes.

Destas, apenas o código fonte da classe **BallManager** precisa ser completado. Você
também deve criar a classe **FxBall** (respeitando as restrições já especificadas para ela) e
pode, eventualmente, criar novas classes que julgar convenientes. A única restrição é que
não são permitidas alterações das assinaturas dos métodos já existentes na classe
**BallManager**.

Adicionalmente, também é fornecido o código fonte da classe **Pong**, para que vocês
possam ter uma visão geral de como o jogo é implementado, mas alterações não devem ser
feitas nesta classe pois, na correção, nossa própria versão da classe será usada. Desta
forma qualquer alteração feita por você na classe **Pong** não será considerada.
