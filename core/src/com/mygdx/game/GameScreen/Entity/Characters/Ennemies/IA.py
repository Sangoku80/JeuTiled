import neat
from flask import Flask, request, jsonify
import json

app = Flask(__name__)

# Empty Collections For Nets and Cars
nets = []
cars = []
current_generation = 0  # Generation counter


@app.route('/postInputs', methods=['POST'])
def postInputs():
    global cars
    try:
        data = request.get_json()
        cars = data
        response = json.dumps({'message': 'Entrées reçues avec succès !'}, ensure_ascii=False)
        print(cars)
        return app.response_class(response, content_type='application/json')
    except Exception as e:
        print(f"Erreur : {e}")
        return jsonify({'error': str(e)}), 500


@app.route('/getOutputs', methods=['GET'])
def getOutputs():
    try:
        response = json.dumps(cars, ensure_ascii=False)
        return app.response_class(response, content_type='application/json')
    except Exception as e:
        print(f"Erreur : {e}")
        return jsonify({'error': str(e)}), 500


def startTraining():
    # Load Config
    config_path = "core/src/com/mygdx/game/GameScreen/Entity/Characters/Ennemies/config.txt"
    config = neat.config.Config(neat.DefaultGenome,
                                neat.DefaultReproduction,
                                neat.DefaultSpeciesSet,
                                neat.DefaultStagnation,
                                config_path)

    # Create Population And Add Reporters
    population = neat.Population(config)
    population.add_reporter(neat.StdOutReporter(True))
    stats = neat.StatisticsReporter()
    population.add_reporter(stats)

    # Run Simulation For A Maximum of 1000 Generations
    population.run(runSimulation, 10)


def runSimulation(genomes, config):
    # For All Genomes Passed Create A New Neural Network
    for i, g in genomes:
        net = neat.nn.FeedForwardNetwork.create(g, config)
        nets.append(net)
        g.fitness = 0

    # # Font Settings
    # generation_font = pygame.font.SysFont("Arial", 30)
    # alive_font = pygame.font.SysFont("Arial", 20)

    global current_generation
    current_generation += 1

    # Simple Counter To Roughly Limit Time (Not Good Practice)
    counter = 0

    while True:

        # For Each Car Get The Acton It Takes
        for i, car in enumerate(cars):
            output = nets[i].activate(car.get_data())
            choice = output.index(max(output))
            if choice == 0:
                car.angle += 10  # Left
            elif choice == 1:
                car.angle -= 10  # Right
            elif choice == 2:
                if car.speed - 2 >= 12:
                    car.speed -= 2  # Slow Down
            else:
                car.speed += 2  # Speed Up

        # Check If Car Is Still Alive
        # Increase Fitness If Yes And Break Loop If Not
        still_alive = 0
        for i, car in enumerate(cars):
            if car.is_alive():
                still_alive += 1
                # car.update(game_map)
                genomes[i][1].fitness += car.get_reward()

        if still_alive == 0:
            break

        counter += 1
        if counter == 30 * 40:  # Stop After About 20 Seconds
            break

        # # Draw Map And All Cars That Are Alive
        # screen.blit(game_map, (0, 0))
        # for car in cars:
        #     if car.is_alive():
        #         car.draw(screen)

        # # Display Info
        # text = generation_font.render("Generation: " + str(current_generation), True, (0, 0, 0))
        # text_rect = text.get_rect()
        # text_rect.center = (900, 450)
        # screen.blit(text, text_rect)
        #
        # text = alive_font.render("Still Alive: " + str(still_alive), True, (0, 0, 0))
        # text_rect = text.get_rect()
        # text_rect.center = (900, 490)
        # screen.blit(text, text_rect)


if __name__ == '__main__':
    # util pour l'échange python/java
    app.run(debug=True)
