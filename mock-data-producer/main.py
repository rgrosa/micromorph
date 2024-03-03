import argparse

import time


from services import Services

try:
    print('Starting Execution!')
    parser = argparse.ArgumentParser(description='json env, see readme', prefix_chars='-')
    parser.add_argument('--s', metavar='schema', type=str, help='choose schema to send data')
    args = parser.parse_args()
    continue_sending = True
    i = 0


    while continue_sending:
        i = i + 1
        print("Start interation = "+ str(i))
        services = Services(args.s)
        payloadData = services.get_data_from_provider()
        micromorphPayload = services.format_payload(payloadData)
        services.send_data_to_micromorph(micromorphPayload)

        print("Finished interation = "+ str(i))
        time.sleep(60)



except Exception as e:
    print(e)
