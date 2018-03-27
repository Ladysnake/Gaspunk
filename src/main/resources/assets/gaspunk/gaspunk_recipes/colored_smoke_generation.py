dyes = ['Black', 'Red', 'Green', 'Brown', 'Blue', 'Purple', 'Cyan', 'LightGray', 'Gray', 'Pink', 'Lime', 'Yellow',
        'LightBlue', 'Magenta', 'Orange', 'White']
stupid_dyes = ['black', 'red', 'green', 'brown', 'blue', 'purple', 'cyan', 'silver', 'gray', 'pink', 'lime', 'yellow',
               'light_blue', 'magenta', 'orange', 'white']
for i in range(len(dyes)):
    dye = dyes[i]
    smoke_name = "colored_smoke_" + stupid_dyes[i]
    file = open(smoke_name + ".json", 'w')
    file.write(
        '{"result": "gaspunk:%(smoke_name)s", "input": {   "gas": "gaspunk:smoke" }, "ingredient": {   "type": "forge:ore_dict",   "ore": "dye%(dye)s" }}' % {
            'dye': dye, 'smoke_name': smoke_name})
