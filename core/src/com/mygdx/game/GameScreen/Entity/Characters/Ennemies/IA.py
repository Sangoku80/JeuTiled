# This Code is Heavily Inspired By The YouTuber: Cheesy AI
# Code Changed, Optimized And Commented By: NeuralNine (Florian Dedov)
import json
import neat
from flask import jsonify, Flask, request
import requests

WIDTH = 1920
HEIGHT = 1080

CAR_SIZE_X = 60
CAR_SIZE_Y = 60

BORDER_COLOR = (255, 255, 255, 255)  # Color To Crash on Hit
URL_INPUTS = 'http://localhost:5000/inputs'
URL_OUTPUTS = 'http://localhost:5000/outputs'

current_generation = 0  # Generation counter

app = Flask(__name__)

inputs = []
outputs = []


# def receive_inputs():
#     try:
#         response = requests.get(URL_INPUTS)
#         if response.status_code == 200:
#             inputs.append(response.text)
#         else:
#             print(f"Erreur {response.status_code} lors de la requête.")
#     except requests.exceptions.RequestException as e:
#         print(f"Une erreur s'est produite lors de la requête : {e}")


@app.route('/inputs', methods=['POST'])
def receive_inputs():
    # Récupération des données JSON envoyées dans la requête
    data = request.json
    print("Données reçues :", data['inputs'])

    # Exemple de traitement des données
    output_data = {'message': 'Traitement réussi', 'input_data': data}

    # Renvoi d'une réponse JSON
    return jsonify(output_data)


def send_outputs():
    data = {
        "outputs": outputs
    }

    json_data = json.dumps(data)
    print(json_data)

    headers = {
        'Content-Type': 'application/json'
    }

    response = requests.post(URL_OUTPUTS, headers=headers, data=json_data)

    if response.status_code != 200:
        raise Exception(f"Unexpected code {response.status_code}")


def get_reward(self):
    # Calculate Reward (Maybe Change?)
    # return self.distance / 50.0
    return self.distance / (CAR_SIZE_X / 2)


def run_simulation(genomes, config):
    # Empty Collections For Nets and Cars
    nets = []
    cars = []

    # For All Genomes Passed Create A New Neural Network
    for i, g in genomes:
        net = neat.nn.FeedForwardNetwork.create(g, config)
        nets.append(net)
        g.fitness = 0

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
                genomes[i][1].fitness += car.get_reward()

        if still_alive == 0:
            break

        counter += 1
        if counter == 30 * 40:  # Stop After About 20 Seconds
            break


if __name__ == "__main__":
    # # Load Config
    # config_path = "./config.txt"
    # config = neat.config.Config(neat.DefaultGenome,
    #                             neat.DefaultReproduction,
    #                             neat.DefaultSpeciesSet,
    #                             neat.DefaultStagnation,
    #                             config_path)
    #
    # # Create Population And Add Reporters
    # population = neat.Population(config)
    # population.add_reporter(neat.StdOutReporter(True))
    # stats = neat.StatisticsReporter()
    # population.add_reporter(stats)
    #
    # # Run Simulation For A Maximum of 1000 Generations
    # population.run(run_simulation, 1000)
    app.run(host='0.0.0.0', port=5000)