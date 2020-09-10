<html>

<h1>🚗 Carona UFC 🚗</h1>
<h3>Esse aplicativo foi desenvolvido para um projeto final da disciplina de Dev. Mobile, ofertado pela UFC</h3>
<h4>Descrição do projeto: <a href="https://moodle2.quixada.ufc.br/pluginfile.php/5841/mod_resource/content/1/ProjetoFinal.pdf">click aqui</a> </h4>

<p>Optei por desenvolver um App de Caronas, pois o mesmo atendia basicamente todos os requisitos que foram requistados pelo professor</p>
<p>Para esse App eu utilizei o banco de dados Firebase, pois iria facilitar muito o meu trabalho nas configurações.</p>
<h2>Tela de login</h2>
<h3>Fazendo Login com Email e Senha</h3>
<p>A autentificação dos usuários foi feita com o FirebaseAuth, pois foi a forma mais simples e rápida...</p>
<p>Nessa tarefa aprendi muito com meus erros e tutoriais no Youtube, StackOverflow, Documentação e etc...</p>
<h4>Verificações e comentários dessa tela</h4>
<ul>
    <li><p>No campo de email é verificado se o email está bem formatado, se ele pertence alguma conta, verificações básicas...</p></li>
    <li><p>No campo de senha é verificado se ela contém no mínimo 6 digitos, se ela pertence a alguma conta, verificações básicas...</p></li>
    <li><p>Tentei deixar essa tela mais simples e harmonica possivel para trazer "calma" ao usuário</p></li>
</ul>
<img src="https://user-images.githubusercontent.com/37156004/92674040-e5d26600-f2f2-11ea-9114-ddd3eeddee78.gif" height="300" width="250"/>

<h3>Fazendo Login com Google</h3>
<p>Essa foi um dos requisitos em que mais gostei de trabalhar pois me deu uma noção muito grande de como as coisas realmente funcionam...</p>
<img src="https://user-images.githubusercontent.com/37156004/92674472-c25beb00-f2f3-11ea-9409-1efb819d0fea.gif" height="300" width="250"/>

<h2>Tela de Criar um nova Conta</h2>
<p>Essa tela é somente uma tela de criar conta normal</p>
<img src="https://user-images.githubusercontent.com/37156004/92674836-8b3a0980-f2f4-11ea-822e-cc468c7928c5.gif" height="300" width="250"/>

<h2>Tela de criar Carona</h2>
<ul>
    <li><p>Nessa tela foi utilizado uma API do Google para pegar o endereço que estava sendo digitado</p></li>
    <li><p>Tem o botão de pegar o local em que o usuario está naquele momento...</p></li>
    <li><p>Foi utilizado um fragmente para o botão de HORA e DATA</p></li>
    <li><p>Logo após o preenchimento dos dados o usuário é redirecionado para uma tela de confirmação</p></li>
    <li><p>Nessa tela ele pode ver o endereço de partida e de chegada no GoogleMaps marcado por pontos, e um rota traçada na cor lilás.</p></li>
    <li><p>A rota foi desenhada com uma api...</p></li>
    
</ul>
<img src="https://user-images.githubusercontent.com/37156004/92674738-50d06c80-f2f4-11ea-9656-ea95e6bef71c.gif" height="300" width="250"/>

<h2>Tela de Procurar Caronas</h2>
<p>Nessa tela temos basicamente duas listas de caronas, caronas gratis e caronas pagas</p>
<p>No SearchView foi implementado de forma que ele unisse as duas listas em uma só e mostrasse ao usuário...</p>
<img src="https://user-images.githubusercontent.com/37156004/92721049-daeff380-f33b-11ea-846f-24d03450e80a.gif" height="300" width="250"/>
<img src="https://user-images.githubusercontent.com/37156004/92721853-1b9c3c80-f33d-11ea-9d2f-97b5691f6074.gif" height="300" width="250"/>


<h2>Tela de navegação com o Menu Drawer</h2>
<p>A maior parte da nevegação do app é feita pelo menu lateral, para facilitar e agilizar o desempenho do usuário.</p>
<ul>
    <li><p>Clicando no nome do usuário somos redirecionado para a página de perfil.</p></li>
    <li><p>Página Inicial</p></li>
    <li><p>Minhas Caronas, é onde fica as caronas em que eu garanti minha vaga.</p></li>
    <li><p>Pesquisar caronas</p></li>
    <li><p>Histórico de caronas, é onde estão todas as caronas em que eu já peguei.</p></li>
    <li><p>Configurações e Enviar Feedback ainda não foram implementados...</p></li>
    <li><p>Compartilhar, envia um link de download do app para um contato do WhatsApp.</p></li>
    <li><p>Sair, faz logout do app.</p></li>
    
</ul>
<img src="https://user-images.githubusercontent.com/37156004/92721987-4be3db00-f33d-11ea-8f16-151045f4e06c.gif" height="300" width="250"/>


<h2>Trocando a foto do usuário</h2>
<p>Quando o usuário cria uma nova conta, por padrão essa conta vem sem foto, para agilizar o processo de criação de conta</p>
<p>Porém você pode adicionar/trocar de foto indo em seu perfil e fazendo upload de uma foto do seu celular...</p>
<img src="https://user-images.githubusercontent.com/37156004/92722031-5bfbba80-f33d-11ea-8fc2-6180f8311d87.gif" height="300" width="250"/>


<h2>Fazendo logout no app</h2>
<p>Basicamente é um logout simples utilizado o FirebaseAuth ou a conta do Google...</p>
<img src="https://user-images.githubusercontent.com/37156004/92722046-61f19b80-f33d-11ea-8dda-cb69a7521fe8.gif" height="300" width="250"/>


<h2>O que ainda falta fazer ?</h2>
<h3>Ainda pretendo adicionar mais funcionalidades no app, como por exemplo:</h4>
<ul>
    <li><h4>Quando alguém adiconar uma carona o app deve notificar os usuários que tem endereço próximo ao local de partida do motorista</h4></li>
    <li><h4>Quando alguém pegar uma carona, deve ser feita uma verificação por SMS ou Email</h4></li>
    <li><h4>As caronas grátis só serão confirmadas pelo motorista, as caronas pagas serão aceitas instantaneamente</h4></li>
    <li><h4>Adicionar uma classificação com os melhores motoristas do app</h4></li>
    <li><h4>Adicionar anuncions e criar uma versão premium</h4></li>
    <li><h4>Desenvolver uma versão Web da aplicação</h4></li>
</ul>
<p>Por enquanto é isso😅</p>
</html>
